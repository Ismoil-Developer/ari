package uz.mrx.arigo.data.repository.order.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import uz.mrx.arigo.data.remote.api.OrderApi
import uz.mrx.arigo.data.remote.request.order.OrderRequest
import uz.mrx.arigo.data.remote.request.order.UpdateOrderRequest
import uz.mrx.arigo.data.remote.response.order.OrderResponse
import uz.mrx.arigo.data.repository.order.OrderRepository
import uz.mrx.arigo.utils.ResultData
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val api: OrderApi
) : OrderRepository {

    override suspend fun createOrder(id: Int, request: OrderRequest) = channelFlow<ResultData<OrderResponse>> {
        try {
            val response = api.postCreateOrder(id, request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    trySend(ResultData.success(body))
                } else {
                    trySend(ResultData.messageText("Response body is null"))
                }
            } else {
                trySend(ResultData.messageText("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            trySend(ResultData.error(e))
        }
    }

    override suspend fun updateOrder(
        id: Int,
        request: UpdateOrderRequest
    ) = channelFlow<ResultData<OrderResponse>> {
        try {
            val response = api.putOrder(id, request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    trySend(ResultData.success(body))
                } else {
                    trySend(ResultData.messageText("Response body is null"))
                }
            } else {
                trySend(ResultData.messageText("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            trySend(ResultData.error(e))
        }
    }

}
