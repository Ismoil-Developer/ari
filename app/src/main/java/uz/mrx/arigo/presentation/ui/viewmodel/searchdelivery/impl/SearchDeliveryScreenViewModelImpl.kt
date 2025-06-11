package uz.mrx.arigo.presentation.ui.viewmodel.searchdelivery.impl

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.mrx.arigo.data.remote.request.order.OrderCancelRequest
import uz.mrx.arigo.data.remote.response.order.OrderCancelResponse
import uz.mrx.arigo.data.remote.response.order.RetryOrderResponse
import uz.mrx.arigo.data.remote.websocket.WebSocketGooEvent
import uz.mrx.arigo.domain.usecase.order.OrderUseCase
import uz.mrx.arigo.presentation.direction.searchdelivery.SearchDeliveryScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.searchdelivery.SearchDeliveryScreenViewModel
import uz.mrx.arigo.utils.flow
import javax.inject.Inject

@HiltViewModel
class SearchDeliveryScreenViewModelImpl @Inject constructor(
    private val direction: SearchDeliveryScreenDirection,
    private val useCase: OrderUseCase
) : SearchDeliveryScreenViewModel, ViewModel() {

    private val _deliveryAcceptedFlow = MutableSharedFlow<WebSocketGooEvent.DeliveryAccepted>()
    override val deliveryAcceptedFlow: SharedFlow<WebSocketGooEvent.DeliveryAccepted> = _deliveryAcceptedFlow

    private val _courierNotFoundFlow = MutableSharedFlow<WebSocketGooEvent.CourierNotFound>()
    override val courierNotFoundFlow: SharedFlow<WebSocketGooEvent.CourierNotFound> = _courierNotFoundFlow

    private val _searchingFlow = MutableSharedFlow<WebSocketGooEvent.Searching>()
    override val searchingFlow: SharedFlow<WebSocketGooEvent.Searching> = _searchingFlow

    private val _directionUpdateFlow = MutableSharedFlow<WebSocketGooEvent.OrderDirectionUpdate>()
    override val directionUpdateFlow: SharedFlow<WebSocketGooEvent.OrderDirectionUpdate> = _directionUpdateFlow

    override val retryOrder = flow<RetryOrderResponse>()

    override fun openOrderDeliveryScreen(coordinates: String) {
        viewModelScope.launch {
            direction.openOrderDeliveryScreen(coordinates)
        }
    }

    override fun openOrderUpdateScreen(id: Int) {
        viewModelScope.launch {
            direction.openOrderDetailScreen(id)
        }
    }

    override fun retryOrder(id: Int) {
        viewModelScope.launch {
            useCase.retryOrder(id).collectLatest {
                it.onSuccess { data -> retryOrder.tryEmit(data) }
                it.onError { err -> Log.e("RetryOrder", "Error: ${err.message}") }
            }
        }
    }

    private suspend fun handleIncomingMessage(message: WebSocketGooEvent) {
        when (message) {
            is WebSocketGooEvent.DeliveryAccepted -> {
                _deliveryAcceptedFlow.emit(message)
            }

            is WebSocketGooEvent.CourierNotFound -> {
                _courierNotFoundFlow.emit(message)
            }

            is WebSocketGooEvent.Searching -> {
                _searchingFlow.emit(message)
            }

            is WebSocketGooEvent.OrderDirectionUpdate -> {
                _directionUpdateFlow.emit(message)
            }

            is WebSocketGooEvent.UnknownMessage -> {
                Log.w("WebSocket", "Unknown message: ${message.raw_message}")
            }

        }

    }

    override fun cancelOrder(id: Int, request: OrderCancelRequest) {
        viewModelScope.launch {
            useCase.cancelOrder(id, request).collectLatest {
                it.onError {

                }
                it.onSuccess {
                    cancelResponse.tryEmit(it)
                }
            }
        }
    }

    override val cancelResponse = flow<OrderCancelResponse>()

    override fun orderCancelScreen(id: Int) {
        viewModelScope.launch {
            direction.openCancelScreen(id)
        }
    }




}
