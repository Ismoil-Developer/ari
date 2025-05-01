package uz.mrx.arigo.presentation.ui.screen.fragment.main.page

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.R
import uz.mrx.arigo.data.model.IntroData
import uz.mrx.arigo.databinding.PageHomeBinding
import uz.mrx.arigo.presentation.adapter.CarouselAdapter
import uz.mrx.arigo.presentation.adapter.MagazineAdapter
import uz.mrx.arigo.presentation.adapter.PharmacyAdapter
import uz.mrx.arigo.presentation.ui.viewmodel.homepage.HomePageViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.homepage.impl.HomePageViewModelImpl
import uz.mrx.arigo.utils.ViewPagerExtensions.addCarouselEffect

@AndroidEntryPoint
class HomePage : Fragment(R.layout.page_home) {

    private val binding: PageHomeBinding by viewBinding(PageHomeBinding::bind)
    private val viewModel: HomePageViewModel by viewModels<HomePageViewModelImpl>()

    private val handler = Handler(Looper.getMainLooper())
    private val slideRunnable = Runnable {
        binding.viewPager.currentItem =
            (binding.viewPager.currentItem + 1) % carouselAdapter.itemCount
    }

    lateinit var carouselAdapter: CarouselAdapter

    lateinit var list: ArrayList<IntroData>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getActiveAddress.collectLatest {
                val address = it.address
                binding.locationTxt.text = address

            }
        }

        carouselAdapter = CarouselAdapter()

        carouselAdapter.submitList(list)

        binding.seeAllMagazine.setOnClickListener {
            viewModel.openShopListScreen(1)
        }

        binding.seeAllPharmacy.setOnClickListener {
            viewModel.openShopListScreen(2)
        }

        binding.locationTxt.setOnClickListener {
            viewModel.openLocationScreen()
        }

        binding.icLocation.setOnClickListener {
            viewModel.openLocationScreen()
        }

        binding.icNotification.setOnClickListener {
            viewModel.openNotification()
        }

        binding.viewPager.apply {
            adapter = carouselAdapter
            addCarouselEffect()
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                restartAutoSlide()
            }
        })


        val magazineAdapter = MagazineAdapter {
            viewModel.openMagazineDetailScreen(it.id)
        }

        val pharmacyAdapter = PharmacyAdapter {

        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.featureResponse.collectLatest { response ->
                response?.let {
                    val magazineList = it.filter { role -> role.role_id == 1 }
                        .flatMap { role -> role.shops ?: emptyList() }

                    val pharmacyList = it.filter { role -> role.role_id == 2 }
                        .flatMap { role -> role.shops ?: emptyList() }

                    magazineAdapter.submitList(magazineList)
                    pharmacyAdapter.submitList(pharmacyList)

                }
            }
        }

        binding.rvMagazine.adapter = magazineAdapter

        binding.rvPharmacy.adapter = pharmacyAdapter

    }

    private fun startAutoSlide() {
        handler.postDelayed(slideRunnable, 3000)
    }

    private fun stopAutoSlide() {
        handler.removeCallbacks(slideRunnable)
    }

    private fun restartAutoSlide() {
        stopAutoSlide()
        startAutoSlide()
    }

    fun loadData() {
        list = ArrayList()
        list.add(IntroData(R.drawable.reklamaa))
        list.add(IntroData(R.drawable.reklamaa))
        list.add(IntroData(R.drawable.reklamaa))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(slideRunnable)
    }

}