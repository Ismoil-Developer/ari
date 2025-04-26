package uz.mrx.arigo.domain.usecase.register

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.request.register.ConfirmRequest
import uz.mrx.arigo.data.remote.request.register.RegisterRequest
import uz.mrx.arigo.data.remote.response.register.ConfirmResponse
import uz.mrx.arigo.data.remote.response.register.RegisterResponse
import uz.mrx.arigo.utils.ResultData

interface RegisterUseCase {

    suspend fun register(request: RegisterRequest): Flow<ResultData<RegisterResponse>>

    suspend fun confirm(request: ConfirmRequest): Flow<ResultData<ConfirmResponse>>


}