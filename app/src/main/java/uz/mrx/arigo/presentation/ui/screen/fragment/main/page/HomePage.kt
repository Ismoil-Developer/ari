package uz.mrx.arigo.presentation.ui.screen.fragment.main.page

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import uz.mrx.arigo.data.local.shp.MySharedPreference
import uz.mrx.arigo.databinding.PageHomeBinding
import uz.mrx.arigo.presentation.adapter.AdvertisingAdapter
import uz.mrx.arigo.presentation.adapter.AssignedAdapter
import uz.mrx.arigo.presentation.adapter.MagazineAdapter
import uz.mrx.arigo.presentation.adapter.PendingSearchAdapter
import uz.mrx.arigo.presentation.adapter.PharmacyAdapter
import uz.mrx.arigo.presentation.ui.viewmodel.homepage.HomePageViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.homepage.impl.HomePageViewModelImpl
import uz.mrx.arigo.utils.ViewPagerExtensions.addCarouselEffect
import javax.inject.Inject

@AndroidEntryPoint
class HomePage : Fragment(R.layout.page_home) {

    private val binding: PageHomeBinding by viewBinding(PageHomeBinding::bind)
    private val viewModel: HomePageViewModel by viewModels<HomePageViewModelImpl>()

    private val handler = Handler(Looper.getMainLooper())

    private val slideRunnable = Runnable {
        binding.viewPager.currentItem =
            (binding.viewPager.currentItem + 1) % advertisingAdapter.itemCount
    }

    @Inject
    lateinit var sharedPreference: MySharedPreference
    lateinit var advertisingAdapter: AdvertisingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.orderContainer.visibility = View.GONE

        Log.d("AAAAAA", "onViewCreated: ${sharedPreference.token}")

//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.activeOrderResponse.collectLatest {
//
////                binding.textView.text = it.deliver_user.full_name
////                binding.textView2.text = it.shop_location.title
////
////                val id = it.id
////
////                binding.activeOrder.setOnClickListener {
////                    viewModel.openOrderDetailScreen(id)
////                }
//
//            }
//        }


        val adapterAssigned = AssignedAdapter {
            viewModel.openOrderDeliveryScreen(it.id)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.assignedResponse.collectLatest {

                adapterAssigned.submitList(it)
//                viewModel.getActiveOrder()
                Log.d("RRRRRRRR", "onViewCreated: ${it.map { it.items }}")
            }
        }

        binding.activeOrder.adapter = adapterAssigned


        val pendingAdapter = PendingSearchAdapter {
            viewModel.openOrderDetailScreen(it.id)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getPendingSearchResponse.collectLatest {
                if (it.isNotEmpty()) {
                    binding.orderContainer.visibility = View.VISIBLE
                    pendingAdapter.submitList(it)
                }
            }
        }

        binding.orderContainer.adapter = pendingAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getActiveAddress.collectLatest {
                val address = it.address
                binding.locationTxt.text = address
            }
        }

        advertisingAdapter = AdvertisingAdapter()


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getAdvertisingResponse.collectLatest {
                advertisingAdapter.submitList(it)
            }
        }

        binding.seeAllMagazine.setOnClickListener {
            viewModel.openShopListScreen(1)
        }

        binding.shopContainer.setOnClickListener {
            viewModel.openShopListScreen(1)
        }

        binding.pharmacyContainer.setOnClickListener {
            viewModel.openShopListScreen(2)
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
            adapter = advertisingAdapter
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
            viewModel.openMagazineDetailScreen(it.id)
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

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(slideRunnable)
    }

}