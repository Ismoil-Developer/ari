package uz.mrx.arigo.presentation.ui.viewmodel.order

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.request.order.OrderFeedBackRequest
import uz.mrx.arigo.data.remote.response.order.OrderFeedBackResponse

interface OrderCompletedScreenViewModel {

    fun openMainScreen()

    fun postFeedBack(id:Int, request: OrderFeedBackRequest)

    val feedBackResponse:Flow<OrderFeedBackResponse>

}