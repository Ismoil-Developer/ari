package uz.mrx.arigo.presentation.ui.screen.fragment.order

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
import uz.mrx.arigo.data.local.shp.MySharedPreference
import uz.mrx.arigo.data.remote.websocket.ClientWebSocketClient
import uz.mrx.arigo.databinding.ScreenSearchDeliveryBinding
import uz.mrx.arigo.presentation.ui.dialog.OrderDialogRetry
import uz.mrx.arigo.presentation.ui.viewmodel.searchdelivery.SearchDeliveryScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.searchdelivery.impl.SearchDeliveryScreenViewModelImpl
import javax.inject.Inject

@AndroidEntryPoint
class SearchDeliveryScreen : Fragment(R.layout.screen_search_delivery) {

    private val binding: ScreenSearchDeliveryBinding by viewBinding(ScreenSearchDeliveryBinding::bind)

    private val viewModel: SearchDeliveryScreenViewModel by viewModels<SearchDeliveryScreenViewModelImpl>()

    @Inject
    lateinit var clientWebSocketClient: ClientWebSocketClient

    @Inject
    lateinit var sharedPreference: MySharedPreference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = sharedPreference.token
        val url = "http://ari.uzfati.uz/ws/goo/connect/"

        clientWebSocketClient.connect(url, token)

        observeWebSocketEvents()

        binding.btnContinue.setOnClickListener {
            Toast.makeText(requireContext(), "Kutish rejimi...", Toast.LENGTH_SHORT).show()
        }

    }

    private fun observeWebSocketEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    clientWebSocketClient.deliveryAccepted.collectLatest { event ->
                        Log.d("SearchDeliveryScreen", "Delivery Accepted: ${event.order_id}")
                        Toast.makeText(
                            requireContext(),
                            "Buyurtma qabul qilindi: ${event.order_id}",
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.openOrderDeliveryScreen(event.latest_coords.toString(), event.order_id)
                    }
                }

                launch {
                    clientWebSocketClient.courierNotFound.collectLatest { event ->
                        Log.d("SearchDeliveryScreen", "Courier Not Found: ${event.id}")

                        val dialog = OrderDialogRetry(
                            context = requireContext(),
                            onRetry = {
                                viewModel.retryOrder(event.id.toInt())
                            },
                            onEdit = {
                                Toast.makeText(requireContext(), "Tahrirlash bosildi", Toast.LENGTH_SHORT).show()
                                viewModel.openOrderUpdateScreen(event.id.toInt())
                            },
                            onCancel = {
                                Toast.makeText(requireContext(), "Bekor qilish bosildi", Toast.LENGTH_SHORT).show()
                                viewModel.orderCancelScreen(event.id.toInt())
                            }
                        )

                        dialog.show()

                        lifecycleScope.launch {
                            viewModel.retryOrder.collectLatest { response ->
                                Toast.makeText(requireContext(), response.detail, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }


            }
        }
    }

}