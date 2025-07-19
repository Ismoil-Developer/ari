package uz.mrx.arigo.presentation.ui.screen.fragment.detail.magazine

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.R
import uz.mrx.arigo.databinding.PageMagazineInfoBinding
import uz.mrx.arigo.presentation.adapter.FeedBackAdapter
import uz.mrx.arigo.presentation.ui.viewmodel.magazinedetail.MagazineDetailScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.magazinedetail.impl.MagazineDetailScreenViewModelImpl

@AndroidEntryPoint
class MagazineInfoPage(private val id: Int):Fragment(R.layout.page_magazine_info) {

    private val binding:PageMagazineInfoBinding by viewBinding(PageMagazineInfoBinding::bind)
    private val viewModel: MagazineDetailScreenViewModel by viewModels<MagazineDetailScreenViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (id != -1){
            viewModel.getFeaturesDetail(id)
        }


        val adapter = FeedBackAdapter{

        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.featuresDetailResponse.collectLatest {
                binding.workTimerTxt.text = it.work_start + " - " + it.work_end
                binding.workLocationTxt.text = it.locations
                binding.workPhoneTxt.text = it.phone_number
                binding.resAboutTxt.text = it.about



                adapter.submitList(it.feedbacks)

                setRatingStars(it.rating)



            }
        }

        binding.rvFeedBack.adapter = adapter

    }

    private fun setRatingStars(rating: Double) {
        val safe = rating.coerceIn(0.0, 5.0)
        val stars = listOf(binding.star1, binding.star2, binding.star3, binding.star4, binding.star5)

        val eps = 1e-6

        stars.forEachIndexed { index, imageView ->
            val starNumber = index + 1
            val drawableRes = if (safe + eps >= starNumber) {
                R.drawable.ic_star_detail   // to'liq yulduz
            } else {
                R.drawable.ic_star_empty    // bo'sh yulduz
            }
            imageView.setImageResource(drawableRes)
        }
    }


}