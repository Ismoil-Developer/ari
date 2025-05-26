package uz.mrx.arigo.presentation.ui.screen.fragment.order

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
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
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import dagger.hilt.android.AndroidEntryPoint
import uz.mrx.arigo.R
import uz.mrx.arigo.databinding.ScreenOrderDeliveryBinding
import uz.mrx.arigo.presentation.ui.viewmodel.order.OrderDeliveryScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.order.impl.OrderDeliveryScreenViewModelImpl
import uz.mrx.arigo.utils.SwipeToRevealView

@AndroidEntryPoint
class OrderDeliveryScreen:Fragment(R.layout.screen_order_delivery),  DrivingSession.DrivingRouteListener {

    private val binding:ScreenOrderDeliveryBinding by viewBinding(ScreenOrderDeliveryBinding::bind)
    private val viewModel:OrderDeliveryScreenViewModel by viewModels<OrderDeliveryScreenViewModelImpl>()

    private lateinit var mapView: MapView
    private var userLocationLayer: UserLocationLayer? = null

    private lateinit var drivingRouter: DrivingRouter
    private var drivingSession: DrivingSession? = null

    private val startPoint = Point(41.2995, 69.2401) // Tashkent
    private val endPoint = Point(41.3112, 69.2797)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Init map and location services
        mapView = binding.mapView

        mapView.mapWindow.map.move(
            CameraPosition(startPoint, 14.0f, 0.0f, 0.0f)
        )

        val mapKit = MapKitFactory.getInstance()
        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow).apply {
            isVisible = true
            isHeadingEnabled = true
        }

        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()

        // Back button
        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.message.setOnClickListener {
            viewModel.openChatScreen()
        }


        binding.swipeView.setOnSwipeCompleteListener(object : SwipeToRevealView.OnSwipeCompleteListener {
            override fun onSwipeCompleted() {
                viewModel.openFindDeliveryScreen() // ✅ Swipe bo‘lganda chaqiriladi
            }
        })


          
        addIcons()

        buildRoute()

    }


    private fun addIcons() {
        // Start icon (user)
        val startBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_kuryer)
        val resizedStartBitmap = Bitmap.createScaledBitmap(startBitmap, 50, 50, false)
        val startPlacemark = mapView.map.mapObjects.addPlacemark(startPoint)
        startPlacemark.setIcon(ImageProvider.fromBitmap(resizedStartBitmap))

        // End icon (courier)
        val endBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_zakaz_delivery)
        val resizedEndBitmap = Bitmap.createScaledBitmap(endBitmap, 50, 50, false)
        val endPlacemark = mapView.map.mapObjects.addPlacemark(endPoint)
        endPlacemark.setIcon(ImageProvider.fromBitmap(resizedEndBitmap))
    }


    private fun buildRoute() {
        val requestPoints = listOf(
            RequestPoint(startPoint, RequestPointType.WAYPOINT, null),
            RequestPoint(endPoint, RequestPointType.WAYPOINT, null)
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
        // Add the route on the map
        mapView.mapWindow.map.mapObjects.addPolyline(route.geometry)

        // Traffic color will automatically adjust to the traffic data
    }

    override fun onDrivingRoutesError(error: com.yandex.runtime.Error) {
        if (error is NetworkError) {
            println("Network error: ${error}")
        } else {
            println("Unknown error: ${error}")
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