package uz.mrx.arigo.presentation.ui.screen.fragment.order

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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

    lateinit var startPoint:Point // Tashkent
    lateinit var shopPoint:Point // London
    lateinit var endPoint:Point

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = binding.mapView

        // Map init
        val mapKit = MapKitFactory.getInstance()
        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow).apply {
            isVisible = true
            isHeadingEnabled = true
        }
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.activeOrderResponse.collectLatest { order ->

                if (order.id != -1) {
                    binding.orderContainerDelivery.visibility = View.VISIBLE
                    binding.orderFind.visibility = View.GONE
                }

                binding.courierRating.text = order.deliver_user.rating.toString()

                binding.orderTime.text = order.delivery_duration_min.toString()
                startPoint = Point(order.courier_location.latitude, order.courier_location.longitude)
                endPoint = Point(order.customer_location.latitude, order.customer_location.longitude)
                shopPoint = Point(order.shop_location.latitude, order.shop_location.longitude )
                binding.courierName.text = order.deliver_user.full_name

                // Telefon chaqiruv
                val phoneNumber = order.deliver_user.phone_number
                binding.call.setOnClickListener {
                    if (phoneNumber.isNotEmpty()) {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                        startActivity(intent)
                    }
                }

                // ❗️startPoint va endPoint tayyor bo‘lgach — shu yerda xaritani harakatlantirish va marshrut chizish
                mapView.mapWindow.map.move(
                    CameraPosition(startPoint, 14.0f, 0.0f, 0.0f)
                )

                addIcons()
                buildRoute()
            }
        }

        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.message.setOnClickListener {
            viewModel.openChatScreen()
        }

        binding.swipeView.setOnSwipeCompleteListener(object : SwipeToRevealView.OnSwipeCompleteListener {
            override fun onSwipeCompleted() {
                viewModel.openFindDeliveryScreen()
            }
        })
    }


    private fun addIcons() {
        // Kuryer
        val startBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_kuryer)
        val resizedStartBitmap = Bitmap.createScaledBitmap(startBitmap, 50, 50, false)
        val startPlacemark = mapView.map.mapObjects.addPlacemark(startPoint)
        startPlacemark.setIcon(ImageProvider.fromBitmap(resizedStartBitmap))

        // Do‘kon
        val shopBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_zakaz_delivery) // ❗️R.drawable.ic_shop — o‘zingizga mos icon qo‘ying
        val resizedShopBitmap = Bitmap.createScaledBitmap(shopBitmap, 50, 50, false)
        val shopPlacemark = mapView.map.mapObjects.addPlacemark(shopPoint)
        shopPlacemark.setIcon(ImageProvider.fromBitmap(resizedShopBitmap))

        // Zakazchik
        val endBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_zakaz_delivery)
        val resizedEndBitmap = Bitmap.createScaledBitmap(endBitmap, 50, 50, false)
        val endPlacemark = mapView.map.mapObjects.addPlacemark(endPoint)
        endPlacemark.setIcon(ImageProvider.fromBitmap(resizedEndBitmap))
    }



    private fun buildRoute() {
        val requestPoints = listOf(
            RequestPoint(startPoint, RequestPointType.WAYPOINT, null),   // Kuryer manzili
            RequestPoint(shopPoint, RequestPointType.WAYPOINT, null),    // Do‘kon manzili
            RequestPoint(endPoint, RequestPointType.WAYPOINT, null)      // Zakazchik manzili
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