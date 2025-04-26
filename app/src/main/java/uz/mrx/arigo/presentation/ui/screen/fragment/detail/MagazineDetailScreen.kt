package uz.mrx.arigo.presentation.ui.screen.fragment.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uz.mrx.arigo.R
import uz.mrx.arigo.databinding.ScreenMagazineDetailBinding
import uz.mrx.arigo.presentation.adapter.MagazineIndicatorAdapter
import uz.mrx.arigo.presentation.adapter.viewpager.MagazineViewPager
import uz.mrx.arigo.presentation.ui.viewmodel.magazinedetail.MagazineDetailScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.magazinedetail.impl.MagazineDetailScreenViewModelImpl

@AndroidEntryPoint
class MagazineDetailScreen:Fragment(R.layout.screen_magazine_detail) {

    private val binding:ScreenMagazineDetailBinding by viewBinding(ScreenMagazineDetailBinding::bind)
    private val viewModel: MagazineDetailScreenViewModel by viewModels<MagazineDetailScreenViewModelImpl>()


    private val args: MagazineDetailScreenArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backRes.setOnClickListener {
            findNavController().popBackStack()
        }

        val list = arrayListOf(
             "Ro'yhat", "Magazin haqida ma'lumot"
        )

        viewModel.getFeaturesDetail(args.id)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.featuresDetailResponse.collect{
                binding.textRestaurant.text = it.title
                if (it.image.isNotEmpty()){
                    Glide.with(requireContext()).load(it.image).into(binding.viewPagerRes)
                }
            }
        }


        binding.viewPager.adapter = MagazineViewPager(requireActivity(), args.id)

        binding.viewPager.isUserInputEnabled = false


        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setText(list[position])
        }.attach()



    }



}