package uz.mrx.arigo.presentation.ui.viewmodel.order

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.response.order.AssignedResponse
import uz.mrx.arigo.data.remote.response.order.OrderPendingSearchResponse

interface OrderPageViewModel {


    val assignedResponse: Flow<List<AssignedResponse>>

    fun openOrderDeliveryScreen(id: Int)

    val orderSearching:Flow<List<OrderPendingSearchResponse>>

    val orderPending:Flow<List<OrderPendingSearchResponse>>

    fun openOrderDetailScreen(id: Int)


}