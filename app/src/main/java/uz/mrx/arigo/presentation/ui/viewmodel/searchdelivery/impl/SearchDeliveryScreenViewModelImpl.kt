package uz.mrx.arigo.presentation.ui.viewmodel.searchdelivery.impl

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.data.remote.request.order.OrderCancelRequest
import uz.mrx.arigo.data.remote.response.order.OrderCancelResponse
import uz.mrx.arigo.data.remote.response.order.RetryOrderResponse
import uz.mrx.arigo.data.remote.websocket.ClientWebSocketClient
import uz.mrx.arigo.data.remote.websocket.WebSocketGooEvent
import uz.mrx.arigo.domain.usecase.order.OrderUseCase
import uz.mrx.arigo.presentation.direction.searchdelivery.SearchDeliveryScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.searchdelivery.SearchDeliveryScreenViewModel
import uz.mrx.arigo.utils.flow
import javax.inject.Inject

@HiltViewModel
class SearchDeliveryScreenViewModelImpl @Inject constructor(
    private val direction: SearchDeliveryScreenDirection,
    private val useCase: OrderUseCase,
    private val clientWebSocketClient: ClientWebSocketClient
) : ViewModel(), SearchDeliveryScreenViewModel {

    // 🔥 WebSocket event flow'lar
    private val _deliveryAcceptedFlow = MutableSharedFlow<WebSocketGooEvent.DeliveryAccepted>(replay = 1)
    override val deliveryAcceptedFlow: SharedFlow<WebSocketGooEvent.DeliveryAccepted> = _deliveryAcceptedFlow

    private val _courierNotFoundFlow = MutableSharedFlow<WebSocketGooEvent.CourierNotFound>(replay = 1)
    override val courierNotFoundFlow: SharedFlow<WebSocketGooEvent.CourierNotFound> = _courierNotFoundFlow

    private val _searchingFlow = MutableSharedFlow<WebSocketGooEvent.Searching>(replay = 1)
    override val searchingFlow: SharedFlow<WebSocketGooEvent.Searching> = _searchingFlow

    private val _directionUpdateFlow = MutableSharedFlow<WebSocketGooEvent.OrderDirectionUpdate>(replay = 1)
    override val directionUpdateFlow: SharedFlow<WebSocketGooEvent.OrderDirectionUpdate> = _directionUpdateFlow


    // --- Order API flow'lar ---
    override val retryOrder = flow<RetryOrderResponse>()
    override val cancelResponse = flow<OrderCancelResponse>()


    init {
        // 🔥 WebSocket eventlarini kuzatish
        observeWebSocketEvents()
    }

    private fun observeWebSocketEvents() {

        Log.d("SearchDeliveryVM", "🧩 ViewModel created, clientWebSocketClient hash: ${clientWebSocketClient.hashCode()}")


        // ✅ DeliveryAccepted
        viewModelScope.launch {
            clientWebSocketClient.deliveryAccepted.collectLatest {
                Log.d("SearchDeliveryVM", "💥 DeliveryAccepted keldi: ${it.order_id}")
                _deliveryAcceptedFlow.emit(it)
            }
        }

        // ✅ CourierNotFound
        viewModelScope.launch {
            clientWebSocketClient.courierNotFound.collectLatest {
                Log.d("SearchDeliveryVM", "⚠️ CourierNotFound: ${it.id}")
                _courierNotFoundFlow.emit(it)
            }
        }

        // ✅ Searching
        viewModelScope.launch {
            clientWebSocketClient.searching.collectLatest {
                Log.d("SearchDeliveryVM", "🔎 Searching: ${it.shop_title}")
                _searchingFlow.emit(it)
            }
        }

        // ✅ OrderDirectionUpdate
        viewModelScope.launch {
            clientWebSocketClient.orderDirectionUpdate.collectLatest {
                Log.d("SearchDeliveryVM", "📍 Direction update: ${it.direction}")
                _directionUpdateFlow.emit(it)
            }
        }
    }


    // --- API funksiyalar ---
    override fun retryOrder(id: Int) {
        viewModelScope.launch {
            useCase.retryOrder(id).collectLatest {
                it.onSuccess { data -> retryOrder.tryEmit(data) }
                it.onError { err ->
                    Log.e("RetryOrder", "❌ Error: ${err.message}")
                }
            }
        }
    }

    override fun cancelOrder(id: Int, request: OrderCancelRequest) {
        viewModelScope.launch {
            useCase.cancelOrder(id, request).collectLatest {
                it.onSuccess { cancelResponse.tryEmit(it) }
                it.onError { err ->
                    Log.e("CancelOrder", "❌ Error: ${err.message}")
                }
            }
        }
    }


    // --- Navigatsiyalar ---
    override fun openOrderDeliveryScreen(coordinates: String, id: Int) {
        viewModelScope.launch {
            direction.openOrderDeliveryScreen(coordinates, id)
        }
    }

    override fun openOrderUpdateScreen(id: Int) {
        viewModelScope.launch {
            direction.openOrderDetailScreen(id)
        }
    }

    override fun orderCancelScreen(id: Int) {
        viewModelScope.launch {
            direction.openCancelScreen(id)
        }
    }

    override fun openMainScreen() {
        viewModelScope.launch {
            direction.openMainScreen()
        }
    }
}
