package uz.mrx.arigo.presentation.ui.screen.fragment.order

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.R
import uz.mrx.arigo.data.remote.websocket.WebSocketGooEvent
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
            // Do nothing here, will react to websocket
        }

        // Observe WebSocket DeliveryAccepted
        observeIncomingOrders()

    }

    private fun observeIncomingOrders() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.deliveryAcceptedFlow.collect { event ->
                    when (event) {
                        is WebSocketGooEvent.DeliveryAccepted -> {
                            Log.d("HomePage", "New order received: $event")
                            viewModel.openFindDeliveryScreen()
                        }

                        else -> {
                            // Boshqa holatlar kerak bo'lsa shu yerga yozamiz
                        }
                    }
                }
            }


        }
    }
}