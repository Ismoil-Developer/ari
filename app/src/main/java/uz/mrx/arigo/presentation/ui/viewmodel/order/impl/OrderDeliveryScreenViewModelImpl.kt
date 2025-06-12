package uz.mrx.arigo.presentation.ui.viewmodel.order.impl

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.data.remote.response.order.ActiveOrderResponse
import uz.mrx.arigo.data.remote.websocket.WebSocketGooEvent
import uz.mrx.arigo.domain.usecase.order.OrderUseCase
import uz.mrx.arigo.presentation.direction.order.OrderDeliveryScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.order.OrderDeliveryScreenViewModel
import uz.mrx.arigo.utils.flow
import javax.inject.Inject

@HiltViewModel
class OrderDeliveryScreenViewModelImpl @Inject constructor(private val direction:OrderDeliveryScreenDirection, private val orderUseCase: OrderUseCase):OrderDeliveryScreenViewModel, ViewModel() {


    private val _directionUpdateFlow = MutableSharedFlow<WebSocketGooEvent.OrderDirectionUpdate>()
    override val directionUpdateFlow: SharedFlow<WebSocketGooEvent.OrderDirectionUpdate> = _directionUpdateFlow


    override fun openChatScreen() {
        viewModelScope.launch {
            direction.openChatScreen()
        }
    }

    override fun openFindDeliveryScreen() {
        viewModelScope.launch {
            direction.openFindDeliveryScreen()
        }
    }

    override val activeOrderResponse = flow<ActiveOrderResponse>()

    override fun getActive(id: Int) {
        viewModelScope.launch {
            orderUseCase.getActiveOrder(id).collectLatest {
                it.onSuccess {
                    activeOrderResponse.tryEmit(it)
                }
                it.onError {
                }
            }
        }
    }


    override fun openOrderUpdateScreen() {
        viewModelScope.launch {
            orderUseCase.observeMessages().collectLatest { message ->
                Log.d("WebSocket", "Message received: $message")
                handleIncomingMessage(message)
            }
        }
    }


    private suspend fun handleIncomingMessage(message: WebSocketGooEvent) {
        when (message) {
            is WebSocketGooEvent.DeliveryAccepted -> {

            }

            is WebSocketGooEvent.CourierNotFound -> {
            }

            is WebSocketGooEvent.Searching -> {
            }

            is WebSocketGooEvent.OrderDirectionUpdate -> {
                _directionUpdateFlow.emit(message)
            }

            is WebSocketGooEvent.UnknownMessage -> {
                Log.w("WebSocket", "Unknown message: ${message.raw_message}")
            }

        }

    }

    override fun openOrderCompletedScreen(id: Int) {
        viewModelScope.launch {
            direction.openOrderCompletedScreen(id)
        }
    }

    override fun openOrderDetailScreen(id: Int) {
        viewModelScope.launch {
            direction.openOrderDetail(id)
        }
    }

    override fun orderCancelScreen(id: Int) {
        viewModelScope.launch {
            direction.openOrderCancelScreen(id)
        }
    }

}