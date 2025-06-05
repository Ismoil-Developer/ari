package uz.mrx.arigo.presentation.ui.viewmodel.order

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.response.order.ActiveOrderResponse

interface OrderDeliveryScreenViewModel {

    fun openChatScreen()

    fun openFindDeliveryScreen()

    val activeOrderResponse: Flow<ActiveOrderResponse>


}