package uz.mrx.arigo.domain.usecase.order.impl

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.request.order.OrderCancelRequest
import uz.mrx.arigo.data.remote.request.order.OrderFeedBackRequest
import uz.mrx.arigo.data.remote.request.order.OrderRequest
import uz.mrx.arigo.data.remote.request.order.UpdateOrderRequest
import uz.mrx.arigo.data.remote.request.order.UpdateOrderRetryRequest
import uz.mrx.arigo.data.remote.response.history.OrderHistoryDetailResponse
import uz.mrx.arigo.data.remote.response.history.OrderHistoryResponse
import uz.mrx.arigo.data.remote.response.order.ActiveOrderResponse
import uz.mrx.arigo.data.remote.response.order.AssignedResponse
import uz.mrx.arigo.data.remote.response.order.OrderCancelResponse
import uz.mrx.arigo.data.remote.response.order.OrderDetailResponse
import uz.mrx.arigo.data.remote.response.order.OrderFeedBackResponse
import uz.mrx.arigo.data.remote.response.order.OrderPendingSearchResponse
import uz.mrx.arigo.data.remote.response.order.OrderResponse
import uz.mrx.arigo.data.remote.response.order.RetryOrderResponse
import uz.mrx.arigo.data.remote.response.order.UpdateOrderRetryResponse
import uz.mrx.arigo.data.remote.websocket.WebSocketGooEvent
import uz.mrx.arigo.data.repository.order.OrderRepository
import uz.mrx.arigo.domain.usecase.order.OrderUseCase
import uz.mrx.arigo.utils.ResultData
import javax.inject.Inject

class OrderUseCaseImpl @Inject constructor(
    private val repository: OrderRepository
) : OrderUseCase {

    override suspend fun createOrder(id: Int, request: OrderRequest): Flow<ResultData<OrderResponse>> {
        return repository.createOrder(id, request)
    }

    override suspend fun updateOrder(
        id: Int,
        request: UpdateOrderRequest
    ): Flow<ResultData<OrderResponse>> = repository.updateOrder(id, request)

    override fun observeMessages(): Flow<WebSocketGooEvent> = repository.observeMessages()

    override suspend fun retryOrder(id: Int): Flow<ResultData<RetryOrderResponse>> = repository.retryOrder(id)

    override suspend fun updateOrderRetry(
        id: Int,
        request: UpdateOrderRetryRequest
    ): Flow<ResultData<UpdateOrderRetryResponse>> = repository.updateOrderRetry(id, request)

    override suspend fun getOrderPending(): Flow<ResultData<List<OrderPendingSearchResponse>>> = repository.getOrderPending()

    override suspend fun getOrderSearch(): Flow<ResultData<List<OrderPendingSearchResponse>>> = repository.getOrderSearch()


    override suspend fun getOrderDetail(id: Int): Flow<ResultData<OrderDetailResponse>> = repository.getOrderDetail(id)

    override suspend fun cancelOrder(
        id: Int,
        request: OrderCancelRequest
    ): Flow<ResultData<OrderCancelResponse>> = repository.cancelOrder(id, request)

    override suspend fun getActiveOrder(id: Int): Flow<ResultData<ActiveOrderResponse>> = repository.getActiveOrder(id)

    override suspend fun getAssignedOrder(): Flow<ResultData<List<AssignedResponse>>> = repository.getAssignedOrder()

    override suspend fun postFeedBack(
        id: Int,
        request: OrderFeedBackRequest
    ): Flow<ResultData<OrderFeedBackResponse>> = repository.postFeedBack(id, request)

    override suspend fun getOrderHistory(): Flow<ResultData<List<OrderHistoryResponse>>> = repository.getOrderHistory()

    override suspend fun getOrderHistoryDetail(id: Int): Flow<ResultData<OrderHistoryDetailResponse>> = repository.getOrderHistoryDetail(id)

}