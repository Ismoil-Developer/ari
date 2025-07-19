package uz.mrx.arigo.presentation.ui.viewmodel.order

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.request.order.UpdateOrderRequest
import uz.mrx.arigo.data.remote.response.location.ActiveAddressResponse
import uz.mrx.arigo.data.remote.response.order.OrderResponse

interface UpdateOrderScreenViewModel {

    fun openSearchScreenViewModel()

    fun updateOrder(id: Int, request: UpdateOrderRequest)

    val updateResponse:Flow<OrderResponse>

    val getActiveAddress:Flow<ActiveAddressResponse>

    fun openAddLocationScreen(id:Int, location:String, orderId:Int)

}