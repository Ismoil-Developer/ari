package uz.mrx.arigo.presentation.ui.viewmodel.location

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.request.location.LocationCreateRequest
import uz.mrx.arigo.data.remote.response.location.LocationActiveResponse
import uz.mrx.arigo.data.remote.response.location.LocationCreateResponse
import uz.mrx.arigo.data.remote.response.location.LocationDeleteResponse

interface LocationScreenViewModel {

     fun openAddLocationScreen(id: Int)

     val getLocation:Flow<List<LocationCreateResponse>>

     fun postLocationIdActive(id:Int, createResponse: LocationCreateRequest)

     val postLocationActiveResponse:Flow<LocationActiveResponse>

     fun openMainScreen()

     val deleteLocation:Flow<LocationDeleteResponse>

     fun deleteLocation(id: Int)

     fun getLocations()

}