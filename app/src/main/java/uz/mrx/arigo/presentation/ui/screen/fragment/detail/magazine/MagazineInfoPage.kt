package uz.mrx.arigo.presentation.ui.screen.fragment.detail.magazine

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.R
import uz.mrx.arigo.data.remote.response.feature.feedback.FeedBackRequest
import uz.mrx.arigo.databinding.DialogFeedBackBinding
import uz.mrx.arigo.databinding.PageMagazineInfoBinding
import uz.mrx.arigo.presentation.adapter.FeedBackAdapter
import uz.mrx.arigo.presentation.ui.dialog.RatingDialogFragment
import uz.mrx.arigo.presentation.ui.viewmodel.magazinedetail.MagazineDetailScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.magazinedetail.impl.MagazineDetailScreenViewModelImpl

@AndroidEntryPoint
class MagazineInfoPage(
    private val id: Int
) : Fragment(R.layout.page_magazine_info) {

    private val binding: PageMagazineInfoBinding by viewBinding(PageMagazineInfoBinding::bind)
    private val viewModel: MagazineDetailScreenViewModel by viewModels<MagazineDetailScreenViewModelImpl>()

    private lateinit var mapView: MapView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (id != -1) {
            viewModel.getFeaturesDetail(id)
        }

        mapView = binding.mapView
        MapKitFactory.initialize(requireContext())

        val adapter = FeedBackAdapter { /* click callback, if needed */ }
        binding.rvFeedBack.adapter = adapter

        // Ma'lumotni observe qilish
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.featuresDetailResponse.collectLatest { detail ->
                binding.workTimerTxt.text = "${detail.work_start} - ${detail.work_end}"
                binding.workLocationTxt.text = detail.locations
                binding.workPhoneTxt.text = detail.phone_number
                binding.resAboutTxt.text = detail.about




                val coordinates = detail.coordinates.coordinates
                if (coordinates.size >= 2) {
                    val lon = coordinates[0]
                    val lat = coordinates[1]
                    val point = Point(lat, lon)

                    mapView.map.move(
                        CameraPosition(point, 16.0f, 0.0f, 0.0f)
                    )

                    val placemark: PlacemarkMapObject = mapView.map.mapObjects.addPlacemark(
                        point,
                        ImageProvider.fromResource(requireContext(), R.drawable.icon_maps_b) // O‘zingni marker ikonkang
                    )

                    placemark.addTapListener(MapObjectTapListener { _, _ ->
                        openNavigationChooser(lat, lon)
                        true
                    })
                }

                detail.coordinates.coordinates
                adapter.submitList(detail.feedbacks)
                setRatingStars(detail.rating)

            }
        }

        // Feedback post natijasini observe qilish
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.feedBackResponse.collectLatest { response ->
                // Muvaffaqiyatli yuborildi
                showSnack("Izoh yuborildi. Rahmat!")
                binding.edtNumber.setText("")
                hideKeyboard()

                // Yangi feedbackni ro‘yxatga qo‘shish / yoki qayta yuklash
                // 1) Agar response list qaytarmasa: yana detail chaqiramiz
                viewModel.getFeaturesDetail(id)

                // Agar server qaytargan obyektni darhol qo‘shmoqchi bo‘lsangiz:
                // val oldList = adapter.currentList.toMutableList()
                // oldList.add(0, response.toFeedbackItem()) // mapping kerak
                // adapter.submitList(oldList)
            }
        }

        // Baholash tugmasi
        binding.btnContinueLn.setOnClickListener {
            val comment = binding.edtNumber.text.toString().trim()
            if (comment.isEmpty()) {
                showSnack("Iltimos, izoh yozing.")
            } else {
                showRatingDialog(comment)
            }
        }
    }

    private fun showRatingDialog(comment: String) {
        val dialog = RatingDialogFragment(comment) { rating ->
            sendFeedback(comment, rating)
        }
        dialog.show(parentFragmentManager, "RatingDialog")
    }

    private fun sendFeedback(comment: String, rating: Int) {
        if (id == -1) return
        val request = FeedBackRequest(comment = comment, rating = rating)
        viewModel.postFeedBack(id, request)
    }

    private fun showSnack(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(binding.edtNumber.windowToken, 0)
    }

    private fun setRatingStars(rating: Double) {
        val safe = rating.coerceIn(0.0, 5.0)
        val stars =
            listOf(binding.star1, binding.star2, binding.star3, binding.star4, binding.star5)
        val eps = 1e-6
        stars.forEachIndexed { index, imageView ->
            val starNumber = index + 1
            val drawableRes = if (safe + eps >= starNumber) {
                R.drawable.ic_star_detail // to'liq yulduz
            } else {
                R.drawable.ic_star_empty // bo'sh yulduz
            }
            imageView.setImageResource(drawableRes)
        }
    }


    private fun openNavigationChooser(lat: Double, lon: Double) {
        val uri = Uri.parse("geo:$lat,$lon?q=$lat,$lon(Manzil)")
        val intent = Intent(Intent.ACTION_VIEW, uri)

        // Telefon ichidagi barcha navigatsiya ilovalari ro'yxati
        val packageManager = requireContext().packageManager
        val activities = packageManager.queryIntentActivities(intent, 0)

        if (activities.isEmpty()) {
            Toast.makeText(requireContext(), "Hech qanday navigator ilovasi topilmadi", Toast.LENGTH_SHORT).show()
            return
        }

        // Navigatsiya ilovalarini tanlash dialogi
        val appNames = activities.map {
            val appLabel = it.loadLabel(packageManager).toString()
            val packageName = it.activityInfo.packageName
            Triple(appLabel, packageName, it) // (nomi, package, ResolveInfo)
        }

        val appLabels = appNames.map { it.first }.toTypedArray()

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Xaritani tanlang")
            .setItems(appLabels) { _, which ->
                val (_, packageName, resolveInfo) = appNames[which]

                val chosenIntent = Intent(Intent.ACTION_VIEW, uri).apply {
                    setPackage(packageName)
                    setClassName(packageName, resolveInfo.activityInfo.name)
                }

                try {
                    startActivity(chosenIntent)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Ilovani ochib bo‘lmadi", Toast.LENGTH_SHORT).show()
                }
            }
            .show()
    }


    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }


}
