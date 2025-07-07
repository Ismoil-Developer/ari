package uz.mrx.arigo.presentation.ui.screen.fragment.location

import android.Manifest
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.ScreenPoint
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.search.*
import com.yandex.mapkit.search.Session.SearchListener
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.R
import uz.mrx.arigo.data.remote.request.location.LocationCreateRequest
import uz.mrx.arigo.databinding.ScreenLocationAddBinding
import uz.mrx.arigo.presentation.ui.viewmodel.location.AddLocationScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.location.impl.AddLocationScreenViewModelImpl

@AndroidEntryPoint
class AddLocationScreen : Fragment(R.layout.screen_location_add), CameraListener {

    private val binding: ScreenLocationAddBinding by viewBinding(ScreenLocationAddBinding::bind)
    private val viewModel: AddLocationScreenViewModel by viewModels<AddLocationScreenViewModelImpl>()

    private val args:AddLocationScreenArgs by navArgs()

    private var isMarkerRaised = false

    private lateinit var searchManager: SearchManager
    private var searchSession: Session? = null
    private lateinit var mapObjects: MapObjectCollection
    private var userLocationLayer: UserLocationLayer? = null
    private var selectedMarker: PlacemarkMapObject? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.id != -1){
            viewModel.locationDetail(args.id)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.locationDetail.collectLatest {
                if (it.id != -1){
                    binding.edtYourLoc.text = it.address
                    binding.edtLocName.setText(it.custom_name)
                }
            }
        }

        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        if (!isGpsEnabled()) {
            Toast.makeText(requireContext(), "📍 GPS yoqilmagan! Iltimos, GPSni yoqing!", Toast.LENGTH_LONG).show()
        }

        if (!isLocationPermissionGranted()) {
            Toast.makeText(requireContext(), "🚫 GPS permission berilmagan! Iltimos, ruxsat bering!", Toast.LENGTH_LONG).show()
            requestLocationPermission()
        }

        val mapView = binding.mapView
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.ONLINE)
        val mapKit = MapKitFactory.getInstance()

        // Foydalanuvchi joylashuvini olish
        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow).apply {
            isVisible = true
            isHeadingEnabled = true
        }

        // Xarita boshlang‘ich joylashuvi
        val startPosition = Point(41.2995, 69.2401)
        mapView.map.move(
            CameraPosition(startPosition, 18.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 1f),
            null
        )

        // Kamera harakatlanishini kuzatish
        mapView.map.addCameraListener(this)

        // MapObjects yaratish
        mapObjects = mapView.map.mapObjects

        // "Saqlash" tugmasi bosilganda APIga yuborish
        binding.btnContinueLn.setOnClickListener {

            if (args.id != -1){
                val markerBottomPoint = getMarkerBottomPointCoordinates()
                val coordinates = "POINT(${markerBottomPoint!!.longitude} ${markerBottomPoint.latitude})"
                Log.d("RRRRRRRR", "onViewCreated: ${markerBottomPoint.longitude} ${markerBottomPoint.latitude}")
                val request = LocationCreateRequest(
                    custom_name = binding.edtLocName.text.toString(),
                    coordinates = coordinates,
                    address = binding.edtYourLoc.text.toString(),
                    active = true
                )
                viewModel.postLocationIdActive(args.id, request)
            }else{
                val markerBottomPoint = getMarkerBottomPointCoordinates()
                val coordinates = "POINT(${markerBottomPoint!!.longitude} ${markerBottomPoint.latitude})"
                val request = LocationCreateRequest(
                    custom_name = binding.edtLocName.text.toString(),
                    coordinates = coordinates,
                    address = binding.edtYourLoc.text.toString(),
                    active = true
                )
                viewModel.addLocation(request)
            }

        }

        // API javobni ko‘rsatish
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addLocationResponse.collectLatest {
                viewModel.openLocationScreen()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.postLocationActiveResponse.collectLatest {
                viewModel.openLocationScreen()
            }
        }

        // 🔍 Zoom funksiyalari
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

        // 📍 GPS tugmasi bosilganda foydalanuvchi joylashuviga o'tish
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
                        mapView.map.move(
                            CameraPosition(userLocation, 18.0f, 0.0f, 0.0f),
                            Animation(Animation.Type.SMOOTH, 1f),
                            null
                        )

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

    }

    private fun getMarkerBottomPointCoordinates(): Point? {
        val markerView = binding.marker
        val mapView = binding.mapView

        // Markerning pastki markaz nuqtasi (center-bottom)
        val markerBottomCenterX = (markerView.x + markerView.width / 2).toDouble()
        val markerBottomCenterY = (markerView.y + markerView.height / 2 + markerView.height / 2).toDouble()

        val screenPoint = ScreenPoint(markerBottomCenterX.toFloat(), markerBottomCenterY.toFloat())

        return mapView.mapWindow.screenToWorld(screenPoint)
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


    // 📍 Kamera harakatlanganda marker o'zgarishi
    override fun onCameraPositionChanged(
        map: Map,
        cameraPosition: CameraPosition,
        p2: CameraUpdateReason,
        finished: Boolean
    ) {
        if (!isMarkerRaised) raiseMarker()

        if (finished) {
            lowerMarker()
            val markerPoint = getMarkerBottomPointCoordinates()
            getAddressFromCoordinates(markerPoint!!)
        }
    }



    private fun raiseMarker() {
        binding.marker.animate()
            .translationY(-20f) // Marker yuqoriga chiqadi
            .setDuration(200)
            .start()
        isMarkerRaised = true
    }

    private fun lowerMarker() {
        binding.marker.animate()
            .translationY(0f) // Marker orqaga tushadi
            .setDuration(200)
            .start()
        isMarkerRaised = false
    }


    private fun getAddressFromCoordinates(point: Point) {
        searchSession?.cancel()
        searchSession = searchManager.submit(
            "${point.latitude},${point.longitude}", // **Aniq koordinatalar so‘rov sifatida berildi**
            Geometry.fromPoint(point),
            SearchOptions().apply { resultPageSize = 1 }, // **Faqat eng yaqin natijani olish**
            object : SearchListener {
                override fun onSearchResponse(response: Response) {
                    val firstResult = response.collection.children.firstOrNull()?.obj
                    val address = firstResult?.metadataContainer
                        ?.getItem(ToponymObjectMetadata::class.java)
                        ?.address?.formattedAddress ?: "Noma’lum manzil" // **To'liq manzil formati**

                    binding.edtYourLoc.setText(address)

                    val coordinatesText = "Lat: ${point.latitude}, Lng: ${point.longitude}"
                    Toast.makeText(
                        requireContext(),
                        "Koordinata: $coordinatesText\nManzil: $address",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onSearchError(error: com.yandex.runtime.Error) {
                    binding.edtYourLoc.setText("Noma’lum manzil")
                }

            }
        )
    }


    // 📌 Marker yangilash (foydalanuvchi tanlagan joyda marker chiqarish)
    private fun updateMarker(point: Point) {
        if (selectedMarker != null) {
            mapObjects.remove(selectedMarker!!)
        }
        selectedMarker = mapObjects.addPlacemark(
            point,
            ImageProvider.fromResource(requireContext(), R.drawable.ic_location_pin)
        )
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
