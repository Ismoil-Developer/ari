package uz.mrx.arigo.presentation.ui.viewmodel.order

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.request.order.OrderCancelRequest
import uz.mrx.arigo.data.remote.request.order.UpdateOrderRetryRequest
import uz.mrx.arigo.data.remote.response.order.OrderCancelResponse
import uz.mrx.arigo.data.remote.response.order.OrderDetailResponse
import uz.mrx.arigo.data.remote.response.order.UpdateOrderRetryResponse

interface OrderDetailScreenViewModel {

    fun updateOrderRetry(id:Int, request: UpdateOrderRetryRequest)

    val updateOrderRetryResponse:Flow<UpdateOrderRetryResponse>


    fun openOrderUpdateRetryScreen(id:Int)

    fun getOrderDetail(id: Int)

    val getOrderDetailResponse:Flow<OrderDetailResponse>

    fun openCancelScreen(id: Int)

    fun cancelOrder(id:Int, request: OrderCancelRequest)

    val cancelResponse:Flow<OrderCancelResponse>

    fun openSearchDeliveryScreen()



}