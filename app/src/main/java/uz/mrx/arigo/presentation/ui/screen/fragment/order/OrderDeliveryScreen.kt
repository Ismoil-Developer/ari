package uz.mrx.arigo.presentation.ui.screen.fragment.order

import android.content.Intent
import android.content.IntentSender
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.R
import uz.mrx.arigo.data.local.shp.MySharedPreference
import uz.mrx.arigo.data.remote.websocket.ClientWebSocketClient
import uz.mrx.arigo.databinding.ScreenOrderDeliveryBinding
import uz.mrx.arigo.presentation.ui.viewmodel.order.OrderDeliveryScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.order.impl.OrderDeliveryScreenViewModelImpl
import uz.mrx.arigo.utils.toast
import javax.inject.Inject

@AndroidEntryPoint
class OrderDeliveryScreen:Fragment(R.layout.screen_order_delivery),  DrivingSession.DrivingRouteListener {

    private val binding:ScreenOrderDeliveryBinding by viewBinding(ScreenOrderDeliveryBinding::bind)
    private val viewModel:OrderDeliveryScreenViewModel by viewModels<OrderDeliveryScreenViewModelImpl>()

    val args:OrderDeliveryScreenArgs by navArgs()

    private lateinit var mapView: MapView
    private var userLocationLayer: UserLocationLayer? = null

    private lateinit var drivingRouter: DrivingRouter
//    private var drivingSession: DrivingSession? = null

    private var courierPlacemark: PlacemarkMapObject? = null

    lateinit var startPoint:Point // Tashkent
    lateinit var shopPoint:Point // London
    lateinit var endPoint:Point

    @Inject
    lateinit var clientWebSocketClient: ClientWebSocketClient

    @Inject
    lateinit var sharedPreference: MySharedPreference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.openMainScreen()
            }
        })

        mapView = binding.mapView

        val token = sharedPreference.token
        val url = "ws://ari-delivery.uz/ws/goo/connect/"

        clientWebSocketClient.connect(url, token)

        observeWebSocketEvents()

        binding.detail.setOnClickListener {
            viewModel.openOrderDetailScreen(args.id)
        }

        // Map init
        val mapKit = MapKitFactory.getInstance()
        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow).apply {
            isVisible = true
            isHeadingEnabled = true
        }

        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()

        viewModel.getActive(args.id)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.activeOrderResponse.collectLatest { order ->

                if (order.id != -1) {
                    binding.orderContainerDelivery.visibility = View.VISIBLE
                    binding.orderFind.visibility = View.GONE
                }

                binding.courierRating.text = order.deliver_user.rating.toString()

                order.deliver_user.avatar?.let { avatarUrl ->
                    if (avatarUrl.isNotEmpty()) {
                        Glide.with(requireContext())
                            .load(avatarUrl)
                            .into(binding.prfDelivery)
                    }
                }

                binding.orderTime.text = order.delivery_duration_min.toString()
                startPoint = Point(order.courier_location.latitude, order.courier_location.longitude)
                endPoint = Point(order.customer_location.latitude, order.customer_location.longitude)
                shopPoint = Point(order.shop_location.latitude, order.shop_location.longitude )
                binding.courierName.text = order.deliver_user.full_name

                if (order.direction == "arrived_to_customer"){
                    viewModel.openOrderCompletedScreen(order.id)
                }

                updateDeliverySteps(order.direction)

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

            }

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




        binding.icBack.setOnClickListener {
            viewModel.openMainScreen()
        }

        binding.message.setOnClickListener {
            viewModel.openChatScreen()
        }

//        binding.swipeView.setOnSwipeCompleteListener(object : SwipeToRevealView.OnSwipeCompleteListener {
//            override fun onSwipeCompleted() {
//                viewModel.openFindDeliveryScreen()
//            }
//        })

    }

    private fun vectorToBitmap(@DrawableRes drawableId: Int, width: Int, height: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(requireContext(), drawableId) ?: return null
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }



    private fun addIcons() {

        // Kuryer
        val startBitmap = vectorToBitmap(R.drawable.ic_bicycle_delivery, 50, 50)
        startBitmap?.let {
            courierPlacemark = mapView.map.mapObjects.addPlacemark(startPoint)
            courierPlacemark?.setIcon(ImageProvider.fromBitmap(it))
        }

        // Do‘kon
        val shopBitmap = vectorToBitmap(R.drawable.ic_shop_delivery, 50, 50)
        shopBitmap?.let {
            val shopPlacemark = mapView.map.mapObjects.addPlacemark(shopPoint)
            shopPlacemark.setIcon(ImageProvider.fromBitmap(it))
        }

        // Zakazchik
        val endBitmap = vectorToBitmap(R.drawable.user_pin, 50, 50)
        endBitmap?.let {
            val endPlacemark = mapView.map.mapObjects.addPlacemark(endPoint)
            endPlacemark.setIcon(ImageProvider.fromBitmap(it))
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


    private fun observeWebSocketEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    clientWebSocketClient.orderDirectionUpdate.collectLatest { event ->
                        Toast.makeText(requireContext(), "${event.direction}", Toast.LENGTH_SHORT).show()
                        updateDeliverySteps(event.direction)

                        if (event.direction == "arrived_to_customer"){
                            viewModel.openOrderCompletedScreen(event.order_id)
                        }
                    }
                }

                launch {
                    clientWebSocketClient.locationUpdate.collectLatest {
                        startPoint = Point(it.latitude, it.longitude)

                        // ❗️Agar placemark mavjud bo‘lsa, uning pozitsiyasini yangilaymiz
                        courierPlacemark?.geometry = startPoint

                        // 🔍 (ixtiyoriy) animatsiya qilish uchun:
                        // courierPlacemark?.setAnimated(true)

                        Log.d("LocationUpdate", "New courier location: ${it.latitude}, ${it.longitude}")
                        toast("${it.latitude} ${it.longitude}")

                    }
                }

                launch {
                    clientWebSocketClient.durationUpdate.collectLatest {

                        binding.orderTime.text = it.duration_min.toString()

                    }
                }


                launch {
                    clientWebSocketClient.orderPrice.collectLatest {
                        Log.d("OOOOOOOOO", "observeWebSocketEvents item_price: ${it.item_price}")
                        Log.d("OOOOOOOOO", "observeWebSocketEvents total_price: ${it.total_price}")
                        Log.d("OOOOOOOOO", "observeWebSocketEvents delivery_price: ${it.delivery_price}")
                        toast(it.item_price)
                        toast(it.total_price)
                        toast(it.delivery_price)
                        binding.cost.text = it.total_price
                    }
                }

            }
        }
    }

    private fun updateDeliverySteps(status: String) {
        val activeColor = ContextCompat.getColor(requireContext(), R.color.buttonBgColor)
        val inactiveColor = Color.parseColor("#DDDDDD")

        // Barchasini default: inactive
        binding.arrivedAtStore.setColorFilter(inactiveColor)
        binding.pickedUp.setColorFilter(inactiveColor)
        binding.enRouteToCustomer.setColorFilter(inactiveColor)
        binding.handedOver.setColorFilter(inactiveColor)

        binding.line.setBackgroundColor(inactiveColor)
        binding.line2.setBackgroundColor(inactiveColor)
        binding.arrivedToCustomer.setBackgroundColor(inactiveColor)

        // Holatga qarab aktivlashtiramiz
        when (status) {
            "arrived_at_store" -> {
                binding.arrivedAtStore.setColorFilter(activeColor)
            }
            "picked_up" -> {
                binding.arrivedAtStore.setColorFilter(activeColor)
                binding.line.setBackgroundColor(activeColor)
                binding.pickedUp.setColorFilter(activeColor)
            }
            "en_route_to_customer" -> {
                binding.arrivedAtStore.setColorFilter(activeColor)
                binding.line.setBackgroundColor(activeColor)
                binding.pickedUp.setColorFilter(activeColor)
                binding.line2.setBackgroundColor(activeColor)
                binding.enRouteToCustomer.setColorFilter(activeColor)
            }
            "arrived_to_customer" -> {
                binding.arrivedAtStore.setColorFilter(activeColor)
                binding.line.setBackgroundColor(activeColor)
                binding.pickedUp.setColorFilter(activeColor)
                binding.line2.setBackgroundColor(activeColor)
                binding.enRouteToCustomer.setColorFilter(activeColor)
                binding.arrivedToCustomer.setBackgroundColor(activeColor)
                binding.handedOver.setColorFilter(activeColor)
            }
        }
    }



    override fun onDrivingRoutes(routes: MutableList<DrivingRoute>) {
        val route = routes.firstOrNull() ?: return
        mapView.mapWindow.map.mapObjects.addPolyline(route.geometry)
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