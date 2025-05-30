package uz.mrx.arigo.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import uz.mrx.arigo.data.remote.request.order.OrderRequest
import uz.mrx.arigo.data.remote.request.order.UpdateOrderRequest
import uz.mrx.arigo.data.remote.response.order.OrderResponse

interface OrderApi {

    @POST("/goo/create_order/{id}/")
    suspend fun postCreateOrder(
        @Path("id") id: Int,
        @Body orderRequest: OrderRequest
    ): Response<OrderResponse>

    @PATCH("/goo/orders/{id}/address/")
    suspend fun putOrder(@Path("id") id: Int, @Body updateOrder: UpdateOrderRequest):Response<OrderResponse>

}