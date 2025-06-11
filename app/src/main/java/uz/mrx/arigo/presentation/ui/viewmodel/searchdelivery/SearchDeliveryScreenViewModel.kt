package uz.mrx.arigo.presentation.ui.viewmodel.searchdelivery

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import uz.mrx.arigo.data.remote.request.order.OrderCancelRequest
import uz.mrx.arigo.data.remote.response.order.OrderCancelResponse
import uz.mrx.arigo.data.remote.response.order.RetryOrderResponse
import uz.mrx.arigo.data.remote.websocket.WebSocketGooEvent

interface SearchDeliveryScreenViewModel {

    fun openOrderDeliveryScreen(coordinates: String, id: Int)

    val deliveryAcceptedFlow: SharedFlow<WebSocketGooEvent.DeliveryAccepted>

    val courierNotFoundFlow: SharedFlow<WebSocketGooEvent.CourierNotFound>

    val searchingFlow: SharedFlow<WebSocketGooEvent.Searching>

    val directionUpdateFlow: SharedFlow<WebSocketGooEvent.OrderDirectionUpdate>

    fun openOrderUpdateScreen(id: Int)

    fun retryOrder(id: Int)

    val retryOrder: Flow<RetryOrderResponse>

    fun cancelOrder(id:Int, request: OrderCancelRequest)

    val cancelResponse:Flow<OrderCancelResponse>

    fun orderCancelScreen(id: Int)

}