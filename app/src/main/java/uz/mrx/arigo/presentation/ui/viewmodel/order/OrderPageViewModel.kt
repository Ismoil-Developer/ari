package uz.mrx.arigo.presentation.ui.viewmodel.order

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.response.order.AssignedResponse

interface OrderPageViewModel {


    val assignedResponse: Flow<List<AssignedResponse>>

    fun openOrderDeliveryScreen(id: Int)

}