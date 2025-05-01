package uz.mrx.arigo.presentation.ui.screen.fragment.shop

import android.Manifest
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.R
import uz.mrx.arigo.data.remote.response.feature.map.MapListResponse
import uz.mrx.arigo.databinding.ScreenMapSearchBinding
import uz.mrx.arigo.presentation.adapter.SearchMapListAdapter
import uz.mrx.arigo.presentation.ui.viewmodel.shop.SearchMapScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.shop.impl.SearchMapScreenViewModelImpl
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.model.LatLng

@AndroidEntryPoint
class SearchMapScreen : Fragment(R.layout.screen_map_search) {

    private val binding: ScreenMapSearchBinding by viewBinding(ScreenMapSearchBinding::bind)
    private val viewModel: SearchMapScreenViewModel by viewModels<SearchMapScreenViewModelImpl>()
    private lateinit var mapView: MapView
    private lateinit var mapObjects: MapObjectCollection
    private var userLocationLayer: UserLocationLayer? = null
    private val args:SearchMapScreenArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isGpsEnabled()) {
            Toast.makeText(requireContext(), "📍 GPS yoqilmagan! Iltimos, GPSni yoqing!", Toast.LENGTH_LONG).show()
        }

        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        if (!isLocationPermissionGranted()) {
            Toast.makeText(requireContext(), "🚫 GPS permission berilmagan! Iltimos, ruxsat bering!", Toast.LENGTH_LONG).show()
            requestLocationPermission()
        }


        mapView = binding.mapView
        mapObjects = mapView.map.mapObjects

        val mapKit = MapKitFactory.getInstance()


        val startPosition = Point(41.2995, 69.2401)
        mapView.map.move(
            CameraPosition(startPosition, 15.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 1f),
            null
        )

        binding.seekBarCount.progress = 10
        binding.radiusCount.text = "10"
        viewModel.getMapList(args.id, 10)

        binding.seekBarCount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Harakat paytida hech narsa qilmaymiz
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Slider harakatga kelganda hech narsa qilmaymiz
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val radius = seekBar?.progress ?: 1
                binding.radiusCount.text = radius.toString()
                viewModel.getMapList(args.id, radius)
            }
        })

        val searchMapListAdapter = SearchMapListAdapter{
            viewModel.openShopDetail(it.id)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchMapListResponse.collectLatest {
                searchMapListAdapter.submitList(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchMapListResponse.collectLatest { shopList ->
                addShopsToMap(shopList)
            }
        }

        binding.rv.adapter = searchMapListAdapter

        // Foydalanuvchi joylashuvini olish
        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow).apply {
            isVisible = true
            isHeadingEnabled = true
        }

        // Agar foydalanuvchi joylashuvi mavjud bo‘lsa, unga o'tish
        val userLocation = userLocationLayer?.cameraPosition()?.target
        if (userLocation != null) {
            moveToLocation(userLocation)
        } else {
            Toast.makeText(requireContext(), "GPS joylashuv aniqlanmagan!", Toast.LENGTH_SHORT).show()
        }

        binding.plus.setOnClickListener {
            val currentPosition = mapView.map.cameraPosition
            mapView.map.move(
                CameraPosition(currentPosition.target, currentPosition.zoom + 1, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 0.5f),
                null
            )
        }

        binding.minus.setOnClickListener {
            val currentPosition = mapView.map.cameraPosition
            mapView.map.move(
                CameraPosition(currentPosition.target, currentPosition.zoom - 1, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 0.5f),
                null
            )
        }


          binding.gps.setOnClickListener {
                val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
                    priority = com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
                }
                val builder = com.google.android.gms.location.LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest)

                val settingsClient =
                    com.google.android.gms.location.LocationServices.getSettingsClient(requireActivity())
                val task = settingsClient.checkLocationSettings(builder.build())

                task.addOnSuccessListener {
                    // GPS yoqilgan, joylashuvni olish
                    getLastKnownLocation { location ->
                        if (location != null) {
                            val userLocation = Point(location.latitude, location.longitude)
                            moveToLocation(userLocation)

                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Joylashuvni olish uchun ruhsat bering",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

                task.addOnFailureListener { exception ->
                    if (exception is com.google.android.gms.common.api.ResolvableApiException) {
                        // GPSni yoqish oynasini ko'rsatish
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
                            "GPSni yoqish talab qilinadi",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

//        // 📍 GPS tugmasi bosilganda foydalanuvchi joylashuviga o'tish
//        binding.gps.setOnClickListener {
//            val location = userLocationLayer?.cameraPosition()?.target
//            if (location != null) {
//                moveToLocation(location)
//            } else {
//                Toast.makeText(requireContext(), "GPS joylashuv aniqlanmagan!", Toast.LENGTH_SHORT).show()
//            }
//        }

    }

    // Xaritada foydalanuvchi joylashuviga o'tish
    private fun moveToLocation(point: Point) {
        mapView.map.move(
            CameraPosition(point, 15.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 1f),
            null
        )
    }

    private fun addShopsToMap(shopList: List<MapListResponse>) {
        mapObjects.clear() // Eskilarni tozalash

        for (shop in shopList) {
            val coordinates = shop.coordinates.coordinates
            if (coordinates.size == 2) {
                val point = Point(coordinates[1], coordinates[0])

                // Markerning orqa fonini bitmapga aylantirish
                val markerBackground = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_marker_search)
                val backgroundBitmap = markerBackground?.toBitmap()

                // Do‘kon rasmini yuklab olib markerga qo‘yish
                Glide.with(requireContext())
                    .asBitmap()
                    .load(shop.image)
                    .apply(RequestOptions.circleCropTransform())
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            // Marker ustiga shop.image ni joylash
                            val finalMarker = overlayImages(backgroundBitmap, resource)

                            // Yangi marker qo‘shish
                            val placemark = mapObjects.addPlacemark(point)
                            placemark.setIcon(ImageProvider.fromBitmap(finalMarker))
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })
            }
        }
    }

    private fun getLastKnownLocation(callback: (android.location.Location?) -> Unit) {
        val fusedLocationClient =
            com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(
                requireActivity()
            )

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
                priority = com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
                interval = 1000 // 1 sekund
                fastestInterval = 500
            }

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    callback(location)
                } else {
                    // Agar oxirgi joylashuv yo'q bo'lsa, real vaqtda joylashuvni oling
                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        object : com.google.android.gms.location.LocationCallback() {
                            override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult) {
                                val currentLocation = locationResult.lastLocation
                                callback(currentLocation)
                                fusedLocationClient.removeLocationUpdates(this) // Faqat bir marta joylashuvni oling
                            }
                        },
                        null
                    )
                }
            }.addOnFailureListener { exception ->
                Log.e("LocationError", "Joylashuvni olishda xatolik: ${exception.message}")
                callback(null)
            }
        } else {
            // Ruxsat so'rash
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            callback(null)
        }
    }

    // Bitmaplarni birlashtirish (marker foni + shop.image)
    private fun overlayImages(background: Bitmap?, foreground: Bitmap): Bitmap {
        if (background == null) return foreground

        val result = Bitmap.createBitmap(background.width, background.height, background.config)
        val canvas = Canvas(result)

        // Marker fonini chizish
        canvas.drawBitmap(background, 0f, 0f, null)

        // Shop rasmni joylash
        val imageSize = background.width / 2
        val x = (background.width - imageSize) / 1.8f
        val y = (background.height - imageSize) / 2.5f

        val resizedForeground = Bitmap.createScaledBitmap(foreground, imageSize, imageSize, false)
        canvas.drawBitmap(resizedForeground, x.toFloat(), y.toFloat(), null)

        return result
    }

    private fun isGpsEnabled(): Boolean {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
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
        binding.mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }
}
