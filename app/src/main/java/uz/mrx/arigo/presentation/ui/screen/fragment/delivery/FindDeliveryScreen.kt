package uz.mrx.arigo.presentation.ui.screen.fragment.delivery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import dagger.hilt.android.AndroidEntryPoint
import uz.mrx.arigo.R
import uz.mrx.arigo.databinding.ScreenFindDeleveryBinding

@AndroidEntryPoint
class FindDeliveryScreen:Fragment(R.layout.screen_find_delevery) {

    private val binding:ScreenFindDeleveryBinding by viewBinding(ScreenFindDeleveryBinding::bind)

    private lateinit var mapView: MapView

    private lateinit var mapObjects: MapObjectCollection

    private var userLocationLayer: UserLocationLayer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = binding.mapView
        mapObjects = mapView.map.mapObjects

        val mapKit = MapKitFactory.getInstance()

        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow).apply {
            isVisible = true
            isHeadingEnabled = true
        }

        val startPosition = Point(41.2995, 69.2401)
        mapView.map.move(
            CameraPosition(startPosition, 15.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 1f),
            null
        )

        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }
}