package uz.mrx.arigo.presentation.ui.screen.fragment.location

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yandex.mapkit.geometry.Point
import uz.mrx.arigo.R
import uz.mrx.arigo.databinding.ScreenShopMapBinding

class ShopMapScreen : Fragment(R.layout.screen_shop_map) {

    private val binding: ScreenShopMapBinding by viewBinding(ScreenShopMapBinding::bind)

    private val myLocation = Point(41.311081, 69.240562) // Toshkent markazi
    private val shopLocation = Point(41.3271, 69.2806)   // Magazinning joylashuvi

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


}
