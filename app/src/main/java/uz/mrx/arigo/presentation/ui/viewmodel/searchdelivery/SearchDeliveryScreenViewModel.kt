package uz.mrx.arigo.presentation.ui.viewmodel.searchdelivery

import kotlinx.coroutines.flow.SharedFlow
import uz.mrx.arigo.data.remote.websocket.WebSocketGooEvent

interface SearchDeliveryScreenViewModel {

    fun openFindDeliveryScreen()

    val deliveryAcceptedFlow: SharedFlow<WebSocketGooEvent.DeliveryAccepted>
    val courierNotFoundFlow: SharedFlow<WebSocketGooEvent.CourierNotFound>


    fun openOrderUpdateScreen()

}