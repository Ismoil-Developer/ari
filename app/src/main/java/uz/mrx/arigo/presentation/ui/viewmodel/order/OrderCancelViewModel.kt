package uz.mrx.arigo.presentation.ui.viewmodel.order

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.request.order.OrderCancelRequest
import uz.mrx.arigo.data.remote.response.order.OrderCancelResponse

interface OrderCancelViewModel {

    fun cancelOrder(id:Int, request: OrderCancelRequest)

    val cancelResponse:Flow<OrderCancelResponse>

    fun openMainScreen()


}