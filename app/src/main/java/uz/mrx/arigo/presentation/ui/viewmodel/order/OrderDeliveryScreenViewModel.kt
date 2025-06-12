package uz.mrx.arigo.presentation.ui.viewmodel.order

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import uz.mrx.arigo.data.remote.response.order.ActiveOrderResponse
import uz.mrx.arigo.data.remote.websocket.WebSocketGooEvent

interface OrderDeliveryScreenViewModel {

    fun openChatScreen()

    fun openFindDeliveryScreen()

    val activeOrderResponse: Flow<ActiveOrderResponse>

    fun getActive(id:Int)
    val directionUpdateFlow: SharedFlow<WebSocketGooEvent.OrderDirectionUpdate>


    fun openOrderUpdateScreen()

    fun openOrderCompletedScreen(id: Int)

    fun openOrderDetailScreen(id: Int)

    fun orderCancelScreen(id: Int)



}