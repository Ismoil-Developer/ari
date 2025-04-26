package uz.mrx.arigo.presentation.ui.viewmodel.register

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.request.register.RegisterRequest
import uz.mrx.arigo.data.remote.response.register.RegisterResponse

interface RegisterScreenViewModel {

    fun openLoginScreen()

    fun openConfirmScreen(phoneNumber:String, code:String)

    fun postRegister(request: RegisterRequest)

    val registerResponse:Flow<RegisterResponse>

    val toastMessage: Flow<String>

}