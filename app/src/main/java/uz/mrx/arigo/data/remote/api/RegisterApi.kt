package uz.mrx.arigo.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import uz.mrx.arigo.data.remote.request.register.ConfirmRequest
import uz.mrx.arigo.data.remote.request.register.RegisterRequest
import uz.mrx.arigo.data.remote.response.register.ConfirmResponse
import uz.mrx.arigo.data.remote.response.register.RegisterResponse

interface RegisterApi {

    @POST("/goo/send-code/")
    suspend fun postRegister(@Body request: RegisterRequest):Response<RegisterResponse>

    @POST("/goo/verify-code/")
    suspend fun postConfirm(@Body request: ConfirmRequest):Response<ConfirmResponse>

}