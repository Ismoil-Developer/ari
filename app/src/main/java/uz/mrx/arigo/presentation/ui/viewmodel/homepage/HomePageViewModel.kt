package uz.mrx.arigo.presentation.ui.viewmodel.homepage

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.request.location.LocationCreateRequest
import uz.mrx.arigo.data.remote.response.feature.RoleResponse
import uz.mrx.arigo.data.remote.response.feature.advertising.AdvertisingResponse
import uz.mrx.arigo.data.remote.response.location.ActiveAddressResponse
import uz.mrx.arigo.data.remote.response.location.LocationCreateResponse
import uz.mrx.arigo.data.remote.response.order.ActiveOrderResponse
import uz.mrx.arigo.data.remote.response.order.AssignedResponse
import uz.mrx.arigo.data.remote.response.order.OrderPendingSearchResponse

interface HomePageViewModel {

    fun openMagazineDetailScreen(id:Int, roleId: Int)

    fun openLocationScreen()

    fun openShopListScreen(id: Int)

    val featureResponse:Flow<List<RoleResponse>>

    fun openNotification()

    val getActiveAddress:Flow<ActiveAddressResponse>

    val getAdvertisingResponse:Flow<List<AdvertisingResponse>>

    val getPendingSearchResponse:Flow<List<OrderPendingSearchResponse>>

    fun openOrderDetailScreen(id: Int)

    val activeOrderResponse:Flow<ActiveOrderResponse>

    fun getActiveOrder(id: Int)

    val assignedResponse:Flow<List<AssignedResponse>>

    fun openOrderDeliveryScreen(id: Int)

    fun addLocation(createRequest: LocationCreateRequest)

    val addLocationResponse:Flow<LocationCreateResponse>

}