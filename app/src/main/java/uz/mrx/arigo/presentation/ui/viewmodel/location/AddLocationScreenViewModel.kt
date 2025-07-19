package uz.mrx.arigo.presentation.ui.viewmodel.location

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.request.location.LocationCreateRequest
import uz.mrx.arigo.data.remote.response.location.LocationActiveResponse
import uz.mrx.arigo.data.remote.response.location.LocationCreateResponse
import uz.mrx.arigo.data.remote.response.location.LocationDetailResponse

interface AddLocationScreenViewModel {

    fun openLocationScreen()

    fun openUpdateScreen(id: Int, orderId:Int)

    fun openUpdateRetryScreen(id: Int, orderId:Int)

    fun addLocation(createRequest: LocationCreateRequest)

    val addLocationResponse: Flow<LocationCreateResponse>

    fun locationDetail(id: Int)

    val locationDetail: Flow<LocationDetailResponse>

    fun postLocationIdActive(id: Int, createResponse: LocationCreateRequest)

    val postLocationActiveResponse: Flow<LocationActiveResponse>

}