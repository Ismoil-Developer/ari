package uz.mrx.arigo.presentation.ui.viewmodel.searchdelivery.impl

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.data.remote.response.order.RetryOrderResponse
import uz.mrx.arigo.data.remote.websocket.WebSocketGooEvent
import uz.mrx.arigo.domain.usecase.order.OrderUseCase
import uz.mrx.arigo.presentation.direction.searchdelivery.SearchDeliveryScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.searchdelivery.SearchDeliveryScreenViewModel
import uz.mrx.arigo.utils.flow
import javax.inject.Inject

@HiltViewModel
class SearchDeliveryScreenViewModelImpl @Inject constructor(private val direction: SearchDeliveryScreenDirection, private val useCase: OrderUseCase):SearchDeliveryScreenViewModel, ViewModel() {

    val _deliveryAcceptedFlow = MutableSharedFlow<WebSocketGooEvent.DeliveryAccepted>()
    override val deliveryAcceptedFlow: SharedFlow<WebSocketGooEvent.DeliveryAccepted> = _deliveryAcceptedFlow

    val _courierNotFoundFlow = MutableSharedFlow<WebSocketGooEvent.CourierNotFound>()
    override val courierNotFoundFlow: SharedFlow<WebSocketGooEvent.CourierNotFound> = _courierNotFoundFlow

    override fun openOrderDeliveryScreen(coordinates: String) {
        viewModelScope.launch {
            direction.openOrderDeliveryScreen(coordinates)
        }
    }

    override fun openOrderUpdateScreen() {

        viewModelScope.launch {
            useCase.observeMessages().collectLatest { message ->
                Log.d("WebSocket", "Message received2: $message")
                handleIncomingMessage(message)
            }
        }

    }


    override val retryOrder = flow<RetryOrderResponse>()

    override fun retryOrder(id: Int) {
        viewModelScope.launch {
            useCase.retryOrder(id).collectLatest {
                it.onSuccess {
                    retryOrder.tryEmit(it)
                }
                it.onError {

                }
            }
        }
    }



    private suspend fun handleIncomingMessage(message: WebSocketGooEvent) {
        when (message) {
            is WebSocketGooEvent.DeliveryAccepted -> {
                _deliveryAcceptedFlow.emit(message) // Emit the NewOrder event directly
            }

            is WebSocketGooEvent.CourierNotFound -> {
                _courierNotFoundFlow.emit(message) // Emit the OrderTimeout event directly
            }

            is WebSocketGooEvent.UnknownMessage -> {

            }
        }
    }

}