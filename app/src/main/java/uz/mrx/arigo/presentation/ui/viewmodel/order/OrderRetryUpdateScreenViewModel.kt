package uz.mrx.arigo.presentation.ui.viewmodel.order

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.request.order.UpdateOrderRetryRequest
import uz.mrx.arigo.data.remote.response.order.OrderDetailResponse
import uz.mrx.arigo.data.remote.response.order.UpdateOrderRetryResponse

interface OrderRetryUpdateScreenViewModel {

    fun updateOrderRetry(id:Int, request: UpdateOrderRetryRequest)

    val updateOrderRetryResponse:Flow<UpdateOrderRetryResponse>

    fun getOrderDetail(id: Int)

    val getOrderDetailResponse:Flow<OrderDetailResponse>



}