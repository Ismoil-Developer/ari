package uz.mrx.arigo.presentation.ui.screen.fragment.order

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.mrx.arigo.R
import uz.mrx.arigo.data.local.shp.MySharedPreference
import uz.mrx.arigo.data.remote.websocket.ClientWebSocketClient
import uz.mrx.arigo.databinding.ScreenSearchDeliveryBinding
import uz.mrx.arigo.presentation.ui.dialog.OrderDialogRetry
import uz.mrx.arigo.presentation.ui.viewmodel.searchdelivery.SearchDeliveryScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.searchdelivery.impl.SearchDeliveryScreenViewModelImpl
import uz.mrx.arigo.utils.toast
import javax.inject.Inject

@AndroidEntryPoint
class SearchDeliveryScreen : Fragment(R.layout.screen_search_delivery) {

    private val binding: ScreenSearchDeliveryBinding by viewBinding(ScreenSearchDeliveryBinding::bind)
    private val viewModel: SearchDeliveryScreenViewModel by viewModels<SearchDeliveryScreenViewModelImpl>()
    private val args: SearchDeliveryScreenArgs by navArgs()

    @Inject
    lateinit var sharedPreference: MySharedPreference

    @Inject
    lateinit var clientWebSocketClient:ClientWebSocketClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ WebSocket eventlarini kuzatamiz
        observeWebSocketEvents()

        // 🔙 Navigatsiya
        binding.icBack.setOnClickListener {
            viewModel.openMainScreen()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.openMainScreen()
                }
            }
        )

        binding.btnContinue.setOnClickListener {
            if (args.id != -1) {
                viewModel.orderCancelScreen(args.id)
            } else toast("Qayta urinib ko‘ring")
        }
    }

    private fun observeWebSocketEvents() {
        Log.d("SearchDeliveryUI", "🚀 observeWebSocketEvents start")


        Log.d("SearchDeliveryUI", "🚀 observeWebSocketEvents start, viewModel hash: ${viewModel.hashCode()}")


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.deliveryAcceptedFlow.collectLatest { event ->
                    Log.d("SearchDeliveryUI", "💥 DeliveryAccepted UI keldi: ${event.order_id}")
                    viewModel.openOrderDeliveryScreen(event.latest_coords.toString(), event.order_id)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.courierNotFoundFlow.collectLatest { event ->
                    Log.d("SearchDeliveryUI", "⚠️ CourierNotFound: ${event.id}")
                    toast("Kuryer topilmadi")
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchingFlow.collectLatest { event ->
                    Log.d("SearchDeliveryUI", "🔎 Searching: ${event.shop_title}")
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.directionUpdateFlow.collectLatest { event ->
                    Log.d("SearchDeliveryUI", "📍 Direction update: ${event.direction}")
                }
            }
        }
    }




//    override fun onResume() {
//        super.onResume()
//        val token = sharedPreference.token
//        val url = "ws://ari-delivery.uz/ws/goo/connect/"
//        val orderId = args.id
//        viewModel.reconnectWebSocket(url, token, orderId)
//    }



}
