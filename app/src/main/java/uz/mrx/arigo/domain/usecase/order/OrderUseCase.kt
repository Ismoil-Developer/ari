package uz.mrx.arigo.domain.usecase.order

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.request.order.OrderCancelRequest
import uz.mrx.arigo.data.remote.request.order.OrderFeedBackRequest
import uz.mrx.arigo.data.remote.request.order.OrderRequest
import uz.mrx.arigo.data.remote.request.order.UpdateOrderRequest
import uz.mrx.arigo.data.remote.request.order.UpdateOrderRetryRequest
import uz.mrx.arigo.data.remote.response.history.OrderHistoryDetailResponse
import uz.mrx.arigo.data.remote.response.history.OrderHistoryResponse
import uz.mrx.arigo.data.remote.response.order.ActiveOrderResponse
import uz.mrx.arigo.data.remote.response.order.AdditionalShopResponse
import uz.mrx.arigo.data.remote.response.order.AssignedResponse
import uz.mrx.arigo.data.remote.response.order.OrderCancelResponse
import uz.mrx.arigo.data.remote.response.order.OrderDetailResponse
import uz.mrx.arigo.data.remote.response.order.OrderFeedBackResponse
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

    suspend fun getOrderPending():Flow<ResultData<List<OrderPendingSearchResponse>>>

    suspend fun getOrderSearch():Flow<ResultData<List<OrderPendingSearchResponse>>>

    suspend fun getOrderDetail(id: Int):Flow<ResultData<OrderDetailResponse>>

    suspend fun getActiveOrder(id: Int):Flow<ResultData<ActiveOrderResponse>>

    suspend fun cancelOrder(id: Int, request: OrderCancelRequest):Flow<ResultData<OrderCancelResponse>>

    suspend fun getAssignedOrder():Flow<ResultData<List<AssignedResponse>>>

    suspend fun postFeedBack(id: Int, request: OrderFeedBackRequest):Flow<ResultData<OrderFeedBackResponse>>

    suspend fun getOrderHistory():Flow<ResultData<List<OrderHistoryResponse>>>

    suspend fun getOrderHistoryDetail(id: Int):Flow<ResultData<OrderHistoryDetailResponse>>

    suspend fun getAdditionalShop(id: Int):Flow<ResultData<AdditionalShopResponse>>

}
