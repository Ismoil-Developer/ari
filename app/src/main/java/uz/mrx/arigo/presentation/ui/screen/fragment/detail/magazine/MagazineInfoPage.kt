package uz.mrx.arigo.presentation.ui.screen.fragment.detail.magazine

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RatingBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.R
import uz.mrx.arigo.data.remote.response.feature.feedback.FeedBackRequest
import uz.mrx.arigo.databinding.PageMagazineInfoBinding
import uz.mrx.arigo.presentation.adapter.FeedBackAdapter
import uz.mrx.arigo.presentation.ui.viewmodel.magazinedetail.MagazineDetailScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.magazinedetail.impl.MagazineDetailScreenViewModelImpl

@AndroidEntryPoint
class MagazineInfoPage(
    private val id: Int
) : Fragment(R.layout.page_magazine_info) {

    private val binding: PageMagazineInfoBinding by viewBinding(PageMagazineInfoBinding::bind)
    private val viewModel: MagazineDetailScreenViewModel by viewModels<MagazineDetailScreenViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (id != -1) {
            viewModel.getFeaturesDetail(id)
        }

        val adapter = FeedBackAdapter { /* click callback, if needed */ }
        binding.rvFeedBack.adapter = adapter

        // Ma'lumotni observe qilish
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.featuresDetailResponse.collectLatest { detail ->
                binding.workTimerTxt.text = "${detail.work_start} - ${detail.work_end}"
                binding.workLocationTxt.text = detail.locations
                binding.workPhoneTxt.text = detail.phone_number
                binding.resAboutTxt.text = detail.about

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
        val dialogView = layoutInflater.inflate(R.layout.dialog_feed_back, null, false)
        val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setNegativeButton("Bekor qilish") { d, _ -> d.dismiss() }
            .setPositiveButton("Yuborish") { _, _ ->
                val r = ratingBar.rating.toInt().coerceAtLeast(1)
                sendFeedback(comment, r)
            }
            .create()

        dialog.show()
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
}
