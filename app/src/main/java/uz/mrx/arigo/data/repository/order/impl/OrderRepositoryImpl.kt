package uz.mrx.arigo.data.repository.order.impl

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import uz.mrx.arigo.data.remote.api.OrderApi
import uz.mrx.arigo.data.remote.request.order.OrderCancelRequest
import uz.mrx.arigo.data.remote.request.order.OrderFeedBackRequest
import uz.mrx.arigo.data.remote.request.order.OrderRequest
import uz.mrx.arigo.data.remote.request.order.UpdateOrderRequest
import uz.mrx.arigo.data.remote.request.order.UpdateOrderRetryRequest
import uz.mrx.arigo.data.remote.response.history.OrderHistoryDetailResponse
import uz.mrx.arigo.data.remote.response.history.OrderHistoryResponse
import uz.mrx.arigo.data.remote.response.location.LocationCreateResponse
import uz.mrx.arigo.data.remote.response.order.ActiveOrderResponse
import uz.mrx.arigo.data.remote.response.order.AssignedResponse
import uz.mrx.arigo.data.remote.response.order.OrderCancelResponse
import uz.mrx.arigo.data.remote.response.order.OrderDetailResponse
import uz.mrx.arigo.data.remote.response.order.OrderFeedBackResponse
import uz.mrx.arigo.data.remote.response.order.OrderPendingSearchResponse
import uz.mrx.arigo.data.remote.response.order.OrderResponse
import uz.mrx.arigo.data.remote.response.order.RetryOrderResponse
import uz.mrx.arigo.data.remote.response.order.UpdateOrderRetryResponse
import uz.mrx.arigo.data.remote.websocket.ClientWebSocketClient
import uz.mrx.arigo.data.remote.websocket.WebSocketGooEvent
import uz.mrx.arigo.data.repository.order.OrderRepository
import uz.mrx.arigo.utils.ResultData
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val api: OrderApi,
    private val client: ClientWebSocketClient
) : OrderRepository {


    private val _webSocketEvents = MutableSharedFlow<WebSocketGooEvent>()
    val webSocketEvents: SharedFlow<WebSocketGooEvent> = _webSocketEvents.asSharedFlow()


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

    override suspend fun cancelOrder(
        id: Int,
        request: OrderCancelRequest
    ) = channelFlow<ResultData<OrderCancelResponse>> {
        try {
            val response = api.cancelOrder(id, request)
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


    override fun observeMessages(): Flow<WebSocketGooEvent> {
        return merge(
            client.courierNotFound,
            client.deliveryAccepted,
            client.orderDirectionUpdate,
            client.searching,
            client.orderPrice
        )
    }


    override suspend fun updateOrderRetry(
        id: Int,
        request: UpdateOrderRetryRequest
    ) = channelFlow<ResultData<UpdateOrderRetryResponse>> {
        try {
            val response = api.retryUpdateOrder(id, request)
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

    override suspend fun retryOrder(id: Int) = channelFlow<ResultData<RetryOrderResponse>> {
        try {

            val response = api.retryOrder(id)

            if (response.isSuccessful){

                val body = response.body()
                if (body != null){
                    trySend(ResultData.success(body))
                }else{
                    trySend(ResultData.messageText("Response bodi is null"))
                }

            }else{
                trySend(ResultData.messageText("Error ${response.code()}: ${response.message()}"))
            }

        }catch (e: Exception) {
            trySend(ResultData.error(e))
        }
    }


    override suspend fun getOrderDetail(id: Int) = channelFlow<ResultData<OrderDetailResponse>> {
        try {

            val response = api.getOrderDetail(id)

            if (response.isSuccessful){

                val body = response.body()
                if (body != null){
                    trySend(ResultData.success(body))
                }else{
                    trySend(ResultData.messageText("Response bodi is null"))
                }

            }else{
                trySend(ResultData.messageText("Error ${response.code()}: ${response.message()}"))
            }

        }catch (e: Exception) {
            trySend(ResultData.error(e))
        }

    }

    override suspend fun getActiveOrder(id: Int) = channelFlow<ResultData<ActiveOrderResponse>> {
        try {

            val response = api.getActiveOrder(id)

            if (response.isSuccessful){

                val body = response.body()
                if (body != null){
                    trySend(ResultData.success(body))
                }else{
                    trySend(ResultData.messageText("Response bodi is null"))
                }

            }else{
                trySend(ResultData.messageText("Error ${response.code()}: ${response.message()}"))
            }

        }catch (e: Exception) {
            trySend(ResultData.error(e))
        }
    }


    override suspend fun getOrderPendingSearch() = channelFlow<ResultData<List<OrderPendingSearchResponse>>> {
        try {
            val response = api.getOrderPendingSearch()
            if (response.isSuccessful) {
                val newsResponse = response.body() as List<OrderPendingSearchResponse>

                trySend(ResultData.success(newsResponse))

            } else {
                trySend(ResultData.messageText(response.message()))
            }
        } catch (e: Exception) {
            trySend(ResultData.messageText(e.message.toString()))
        }
    }.catch { emit(ResultData.error(it)) }

    override suspend fun getAssignedOrder() = channelFlow<ResultData<List<AssignedResponse>>> {
        try {
            val response = api.getAssignedOrder()
            if (response.isSuccessful) {
                val newsResponse = response.body() as List<AssignedResponse>

                trySend(ResultData.success(newsResponse))

            } else {
                trySend(ResultData.messageText(response.message()))
            }
        } catch (e: Exception) {
            trySend(ResultData.messageText(e.message.toString()))
        }
    }.catch { emit(ResultData.error(it)) }


    override suspend fun postFeedBack(
        id: Int,
        request: OrderFeedBackRequest
    ) = channelFlow<ResultData<OrderFeedBackResponse>> {
        try {
            val response = api.postFeedBack(id, request)
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

    override suspend fun getOrderHistory() = channelFlow<ResultData<List<OrderHistoryResponse>>> {
        try {
            val response = api.getHistory()
            if (response.isSuccessful) {
                val newsResponse = response.body() as List<OrderHistoryResponse>

                trySend(ResultData.success(newsResponse))

            } else {
                trySend(ResultData.messageText(response.message()))
            }
        } catch (e: Exception) {
            trySend(ResultData.messageText(e.message.toString()))
        }
    }.catch { emit(ResultData.error(it)) }

    override suspend fun getOrderHistoryDetail(id: Int) = channelFlow<ResultData<OrderHistoryDetailResponse>> {
        try {
            val response = api.getHistoryById(id)
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