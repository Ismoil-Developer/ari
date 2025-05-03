package uz.mrx.arigo.data.repository.order

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.request.order.OrderRequest
import uz.mrx.arigo.data.remote.request.order.UpdateOrderRequest
import uz.mrx.arigo.data.remote.response.order.OrderResponse
import uz.mrx.arigo.data.remote.websocket.WebSocketGooEvent
import uz.mrx.arigo.utils.ResultData

interface OrderRepository {

    suspend fun createOrder(id: Int, request: OrderRequest): Flow<ResultData<OrderResponse>>

    suspend fun updateOrder(id: Int, request: UpdateOrderRequest): Flow<ResultData<OrderResponse>>


    fun observeMessages(): Flow<WebSocketGooEvent>


}
