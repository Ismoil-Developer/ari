package uz.mrx.arigo.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
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

interface OrderApi {

    @POST("/goo/create_order/{id}/")
    suspend fun postCreateOrder(
        @Path("id") id: Int,
        @Body orderRequest: OrderRequest
    ): Response<OrderResponse>

    @PATCH("/goo/orders/{id}/address/")
    suspend fun putOrder(@Path("id") id: Int, @Body updateOrder: UpdateOrderRequest):Response<OrderResponse>

    @POST("/goo/orders/{id}/retry/")
    suspend fun retryOrder(@Path("id") id: Int):Response<RetryOrderResponse>

    @PATCH("/goo/orders/{id}/retry-update/")
    suspend fun retryUpdateOrder(@Path("id") id: Int, @Body updateOrderRetryResponse: UpdateOrderRetryRequest):Response<UpdateOrderRetryResponse>

    @GET("/goo/orders/pending-searching/")
    suspend fun getOrderPendingSearch():Response<List<OrderPendingSearchResponse>>

    @GET("/goo/orders/{id}/")
    suspend fun getOrderDetail(@Path("id") id: Int):Response<OrderDetailResponse>

    @GET("/goo/orders/active/{id}")
    suspend fun getActiveOrder(@Path("id") id: Int):Response<ActiveOrderResponse>

    @GET("/goo/orders/assigned/")
    suspend fun getAssignedOrder():Response<List<AssignedResponse>>

    @POST("/goo/order/{id}/feedback/")
    suspend fun postFeedBack(@Path("id") id: Int, @Body request: OrderFeedBackRequest):Response<OrderFeedBackResponse>

    @POST("/goo/order/{id}/cancel/")
    suspend fun cancelOrder(@Path("id") id: Int, @Body request: OrderCancelRequest):Response<OrderCancelResponse>

    @GET("/goo/orders/history/")
    suspend fun getHistory():Response<List<OrderHistoryResponse>>

    @GET("/goo/orders/history/{id}")
    suspend fun getHistoryById(@Path("id") id: Int):Response<OrderHistoryDetailResponse>

}