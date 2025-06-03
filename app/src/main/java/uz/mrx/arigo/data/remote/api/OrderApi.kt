package uz.mrx.arigo.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import uz.mrx.arigo.data.remote.request.order.OrderRequest
import uz.mrx.arigo.data.remote.request.order.UpdateOrderRequest
import uz.mrx.arigo.data.remote.request.order.UpdateOrderRetryRequest
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



}