package uz.mrx.arigo.presentation.ui.screen.fragment.main.page

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
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
import uz.mrx.arigo.data.remote.request.location.LocationCreateRequest
import uz.mrx.arigo.databinding.PageHomeBinding
import uz.mrx.arigo.presentation.adapter.AdvertisingAdapter
import uz.mrx.arigo.presentation.adapter.AssignedAdapter
import uz.mrx.arigo.presentation.adapter.MagazineAdapter
import uz.mrx.arigo.presentation.adapter.PendingAdapter
import uz.mrx.arigo.presentation.adapter.PharmacyAdapter
import uz.mrx.arigo.presentation.ui.viewmodel.homepage.HomePageViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.homepage.impl.HomePageViewModelImpl
import uz.mrx.arigo.utils.ViewPagerExtensions.addCarouselEffect
import javax.inject.Inject
import com.yandex.mapkit.search.*
import com.yandex.runtime.Error
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError
import android.location.LocationManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.Session.SearchListener

@AndroidEntryPoint
class HomePage : Fragment(R.layout.page_home) {

    private val binding: PageHomeBinding by viewBinding(PageHomeBinding::bind)
    private val viewModel: HomePageViewModel by viewModels<HomePageViewModelImpl>()

    private val handler = Handler(Looper.getMainLooper())

    private val slideRunnable = Runnable {
        binding.viewPager.currentItem =
            (binding.viewPager.currentItem + 1) % advertisingAdapter.itemCount
    }

    private lateinit var searchManager: SearchManager
    private var searchSession: Session? = null

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }

    var count:Int = -1

    @Inject
    lateinit var sharedPreference: MySharedPreference
    lateinit var advertisingAdapter: AdvertisingAdapter

    private var doubleBackToExitPressedOnce = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (doubleBackToExitPressedOnce) {
                        requireActivity().finish()
                    } else {
                        doubleBackToExitPressedOnce = true
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.ilovadan_chiqish_uchun_yana_bir_marta_orqaga_tugmasini_bosing),
                            Toast.LENGTH_SHORT
                        ).show()

                        Handler(Looper.getMainLooper()).postDelayed({
                            doubleBackToExitPressedOnce = false
                        }, 2000) // 2 second delay
                    }
                }
            })


        MapKitFactory.initialize(requireContext())
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
        getUserLocationAndSend()

        Log.d("AAAAAA", "onViewCreated: ${sharedPreference.token}")

        if (!isGpsEnabled()) {
            val locationRequest = LocationRequest.create().apply {
                priority = Priority.PRIORITY_HIGH_ACCURACY
            }

            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true) // muhim: dialog har doim ko'rsatiladi

            val settingsClient = LocationServices.getSettingsClient(requireActivity())
            val task = settingsClient.checkLocationSettings(builder.build())

            task.addOnSuccessListener {
                // GPS allaqachon yoqilgan
            }

            task.addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        exception.startResolutionForResult(requireActivity(), 1001)
                    } catch (sendEx: IntentSender.SendIntentException) {
                        Toast.makeText(
                            requireContext(),
                            "GPSni yoqish muammosi yuz berdi",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "📍 GPS yoqilmagan! Iltimos, GPSni yoqing!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }


        if (!isLocationPermissionGranted()) {
            Toast.makeText(requireContext(), "🚫 GPS permission berilmagan! Iltimos, ruxsat bering!", Toast.LENGTH_LONG).show()
            requestLocationPermission()
        }


        val adapterAssigned = AssignedAdapter {
            viewModel.openOrderDeliveryScreen(it.id)
        }

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.assignedResponse.collectLatest {
                adapterAssigned.submitList(it)
            }

        }

        binding.activeOrder.adapter = adapterAssigned


//            val pendingAdapter = PendingAdapter {
//            viewModel.openOrderDetailScreen(it.id)
//        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getActiveAddress.collectLatest {

                if (it.address.isNotEmpty()){
                    val address = it.address
                    binding.locationTxt.text = address
                    count = -1
                    Log.d("AAAAAAAAAAAA", "onViewCreated: $count")
                }else{
                    count = 1
                }

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

        binding.fastContainer.setOnClickListener {
            viewModel.openShopListScreen(1)
        }

        binding.aptekaContainer.setOnClickListener {
            viewModel.openShopListScreen(2)
        }

        binding.seeAllPharmacy.setOnClickListener {
            viewModel.openShopListScreen(2)
        }

        binding.locationEdt.setOnClickListener {
            viewModel.openLocationScreen()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001) {
            if (resultCode == Activity.RESULT_OK) {
                // Foydalanuvchi GPSni yoqdi
                getUserLocationAndSend()
            } else {
                Toast.makeText(requireContext(), "GPS yoqilmadi!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun getUserLocationAndSend() {
        val context = requireContext()

        if (
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1000
            )
            return
        }

        val locationRequest = LocationRequest.create().apply {
            priority = Priority.PRIORITY_HIGH_ACCURACY
            interval = 1000 // so‘rovlar oraliq vaqti
            fastestInterval = 500
            numUpdates = 1 // faqat 1 marotaba joylashuvni olamiz
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val point = Point(latitude, longitude)
                    val coordinatesStr = "POINT($longitude $latitude)"

                    getAddressFromCoordinates(point) { address ->
                        val request = LocationCreateRequest(
                            custom_name = "",
                            coordinates = coordinatesStr,
                            address = address,
                            active = true
                        )
                        if (count != -1) {
                            viewModel.addLocation(request)
                        }
                    }
                } else {
                    Toast.makeText(context, "Joylashuvni aniqlab bo‘lmadi", Toast.LENGTH_SHORT).show()
                }

                // Endi locationCallback kerak emas — to‘xtatamiz
                fusedLocationClient.removeLocationUpdates(this)
            }
        }

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            Toast.makeText(context, "Joylashuv uchun ruxsat yo‘q", Toast.LENGTH_SHORT).show()
        }
    }




    private fun getAddressFromCoordinates(point: Point, callback: (String) -> Unit) {
        searchSession?.cancel()
        searchSession = searchManager.submit(
            "${point.latitude},${point.longitude}",
            Geometry.fromPoint(point),
            SearchOptions().apply { resultPageSize = 1 },
            object : SearchListener {
                override fun onSearchResponse(response: Response) {
                    val firstResult = response.collection.children.firstOrNull()?.obj
                    val address = firstResult?.metadataContainer
                        ?.getItem(ToponymObjectMetadata::class.java)
                        ?.address?.formattedAddress ?: "Noma’lum manzil"

                    callback(address)
                }

                override fun onSearchError(error: Error) {
                    val errorMsg = when (error) {
                        is RemoteError -> "Masofaviy xatolik"
                        is NetworkError -> "Tarmoq xatoligi"
                        else -> "Noma’lum xatolik"
                    }
                    Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
                    callback("Noma’lum manzil")
                }
            }
        )
    }


    private fun isGpsEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1000) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                getUserLocationAndSend()
            } else {
                Toast.makeText(requireContext(), "📍 Lokatsiya uchun ruxsat kerak", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun isLocationPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
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