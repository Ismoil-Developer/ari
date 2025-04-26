package uz.mrx.arigo.presentation.ui.viewmodel.location

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.request.location.LocationCreateRequest
import uz.mrx.arigo.data.remote.response.location.LocationCreateResponse

interface AddLocationScreenViewModel {

    fun openLocationScreen()

    fun addLocation(createRequest: LocationCreateRequest)

    val addLocationResponse:Flow<LocationCreateResponse>

}