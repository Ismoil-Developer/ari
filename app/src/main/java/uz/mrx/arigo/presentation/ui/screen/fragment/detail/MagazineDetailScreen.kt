package uz.mrx.arigo.presentation.ui.screen.fragment.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
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

        Log.d("IIIIIIIII", "onViewCreated: ${args.id}")

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.featuresDetailResponse.collectLatest{
                binding.textRestaurant.text = it.title
                Log.d("YYYYYY", "onViewCreated: ${it.image}")
                if (!it.image.isNullOrEmpty()) {
                    Glide.with(requireContext())
                        .load(it.image)
                        .into(binding.viewPagerRes)
                    Log.d("IIIII", "image: ${it.image}")
                }

            }
        }

        binding.viewPager.adapter = MagazineViewPager(requireActivity(), args.id)

        binding.viewPager.isUserInputEnabled = false

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val tabView = layoutInflater.inflate(R.layout.item_custom_tab, null)
            val titleText = tabView.findViewById<AppCompatTextView>(R.id.tabTitle)
            titleText.text = list[position]
            tab.customView = tabView
        }.attach()





// Tab tanlanganda matn rangini yangilash:
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val text = tab.customView?.findViewById<AppCompatTextView>(R.id.tabTitle)
                text?.setTextColor(resources.getColor(android.R.color.white))
                tab.customView?.isSelected = true

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val text = tab.customView?.findViewById<AppCompatTextView>(R.id.tabTitle)
                text?.setTextColor(resources.getColor(R.color.buttonBgColor))
                tab.customView?.isSelected = false
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


    }



}