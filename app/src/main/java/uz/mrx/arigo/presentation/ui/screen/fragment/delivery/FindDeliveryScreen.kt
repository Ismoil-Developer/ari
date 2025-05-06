package uz.mrx.arigo.presentation.ui.screen.fragment.delivery

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import dagger.hilt.android.AndroidEntryPoint
import uz.mrx.arigo.R
import uz.mrx.arigo.databinding.ScreenFindDeleveryBinding

@AndroidEntryPoint
class FindDeliveryScreen : Fragment(R.layout.screen_find_delevery), DrivingSession.DrivingRouteListener {

    private val binding: ScreenFindDeleveryBinding by viewBinding(ScreenFindDeleveryBinding::bind)

    private lateinit var mapView: MapView
    private lateinit var mapObjects: MapObjectCollection
    private lateinit var drivingRouter: DrivingRouter
    private var drivingSession: DrivingSession? = null
    private var userLocationLayer: UserLocationLayer? = null
    private val args: FindDeliveryScreenArgs by navArgs()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var userPoint: Point? = null
    private lateinit var courierPoint: Point

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init map and location services
        mapView = binding.mapView
        mapObjects = mapView.map.mapObjects
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val mapKit = MapKitFactory.getInstance()
        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow).apply {
            isVisible = true
            isHeadingEnabled = true
        }

        // Init route builder
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()

        // Hardcoded courier location
        courierPoint = Point(41.2995, 69.2401)

        // Back button
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        getUserLocationAndDrawRoute()
    }

    private fun getUserLocationAndDrawRoute() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1001
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                userPoint = Point(location.latitude, location.longitude)

                mapView.map.move(
                    CameraPosition(userPoint!!, 15.0f, 0.0f, 0.0f),
                    Animation(Animation.Type.SMOOTH, 1f),
                    null
                )

                addIcons()
                buildRoute()
            }
        }
    }

    private fun addIcons() {
        // User icon
        val startBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_kuryer)
        val resizedStartBitmap = Bitmap.createScaledBitmap(startBitmap, 50, 50, false)
        val startPlacemark = mapObjects.addPlacemark(userPoint!!)
        startPlacemark.setIcon(ImageProvider.fromBitmap(resizedStartBitmap))

        // Courier icon
        val endBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_zakaz_delivery)
        val resizedEndBitmap = Bitmap.createScaledBitmap(endBitmap, 50, 50, false)
        val endPlacemark = mapObjects.addPlacemark(courierPoint)
        endPlacemark.setIcon(ImageProvider.fromBitmap(resizedEndBitmap))
    }

    private fun buildRoute() {
        val start = userPoint ?: return
        val requestPoints = listOf(
            RequestPoint(start, RequestPointType.WAYPOINT, null),
            RequestPoint(courierPoint, RequestPointType.WAYPOINT, null)
        )

        val drivingOptions = DrivingOptions()
        val vehicleOptions = VehicleOptions()

        drivingSession = drivingRouter.requestRoutes(
            requestPoints,
            drivingOptions,
            vehicleOptions,
            this
        )
    }

    override fun onDrivingRoutes(routes: MutableList<DrivingRoute>) {
        val route = routes.firstOrNull() ?: return
        mapObjects.addPolyline(route.geometry)
    }

    override fun onDrivingRoutesError(error: com.yandex.runtime.Error) {
        if (error is NetworkError) {
            println("Network error: $error")
        } else {
            println("Unknown error: $error")
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}
