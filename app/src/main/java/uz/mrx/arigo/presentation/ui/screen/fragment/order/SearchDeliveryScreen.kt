package uz.mrx.arigo.presentation.ui.screen.fragment.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import dagger.hilt.android.AndroidEntryPoint
import uz.mrx.arigo.R
import uz.mrx.arigo.databinding.ScreenSearchDeliveryBinding
import uz.mrx.arigo.presentation.ui.viewmodel.searchdelivery.SearchDeliveryScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.searchdelivery.impl.SearchDeliveryScreenViewModelImpl

@AndroidEntryPoint
class SearchDeliveryScreen : Fragment(R.layout.screen_search_delivery) {

    private val binding: ScreenSearchDeliveryBinding by viewBinding(ScreenSearchDeliveryBinding::bind)

    private val viewModel: SearchDeliveryScreenViewModel by viewModels<SearchDeliveryScreenViewModelImpl>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnContinue.setOnClickListener {
            viewModel.openFindDeliveryScreen()
        }

    }
}
