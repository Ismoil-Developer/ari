package uz.mrx.arigo.presentation.ui.viewmodel.confirm

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.request.register.ConfirmRequest
import uz.mrx.arigo.data.remote.response.register.ConfirmResponse

interface ConfirmScreenViewModel {

    fun openScreen()

    fun postConfirm(request: ConfirmRequest)

    val confirmResponse:Flow<ConfirmResponse>

}