package uz.mrx.arigo.domain.usecase.order

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.request.order.OrderCancelRequest
import uz.mrx.arigo.data.remote.request.order.OrderRequest
import uz.mrx.arigo.data.remote.request.order.UpdateOrderRequest
import uz.mrx.arigo.data.remote.request.order.UpdateOrderRetryRequest
import uz.mrx.arigo.data.remote.response.order.ActiveOrderResponse
import uz.mrx.arigo.data.remote.response.order.OrderCancelResponse
import uz.mrx.arigo.data.remote.response.order.OrderDetailResponse
import uz.mrx.arigo.data.remote.response.order.OrderPendingSearchResponse
import uz.mrx.arigo.data.remote.response.order.OrderResponse
import uz.mrx.arigo.data.remote.response.order.RetryOrderResponse
import uz.mrx.arigo.data.remote.response.order.UpdateOrderRetryResponse
import uz.mrx.arigo.data.remote.websocket.WebSocketGooEvent
import uz.mrx.arigo.utils.ResultData

interface OrderUseCase {

    suspend fun createOrder(id: Int, request: OrderRequest): Flow<ResultData<OrderResponse>>

    suspend fun updateOrder(id: Int, request: UpdateOrderRequest):Flow<ResultData<OrderResponse>>

    fun observeMessages(): Flow<WebSocketGooEvent>

    suspend fun retryOrder(id: Int):Flow<ResultData<RetryOrderResponse>>

    suspend fun updateOrderRetry(id: Int, request: UpdateOrderRetryRequest):Flow<ResultData<UpdateOrderRetryResponse>>

    suspend fun getOrderPendingSearch():Flow<ResultData<List<OrderPendingSearchResponse>>>

    suspend fun getOrderDetail(id: Int):Flow<ResultData<OrderDetailResponse>>

    suspend fun getActiveOrder():Flow<ResultData<ActiveOrderResponse>>


    suspend fun cancelOrder(id: Int, request: OrderCancelRequest):Flow<ResultData<OrderCancelResponse>>


}
