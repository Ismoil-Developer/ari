package uz.mrx.arigo.presentation.ui.viewmodel.location.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.data.remote.request.location.LocationCreateRequest
import uz.mrx.arigo.data.remote.response.location.LocationActiveResponse
import uz.mrx.arigo.data.remote.response.location.LocationCreateResponse
import uz.mrx.arigo.data.remote.response.location.LocationDetailResponse
import uz.mrx.arigo.domain.usecase.location.LocationUseCase
import uz.mrx.arigo.presentation.direction.location.AddLocationScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.location.AddLocationScreenViewModel
import uz.mrx.arigo.utils.flow
import javax.inject.Inject

@HiltViewModel
class AddLocationScreenViewModelImpl @Inject constructor(private val direction:AddLocationScreenDirection, private val useCase: LocationUseCase):AddLocationScreenViewModel, ViewModel() {

    override fun openLocationScreen() {
        viewModelScope.launch {
            direction.openLocationScreen()
        }
    }

    override fun addLocation(createRequest: LocationCreateRequest) {
        viewModelScope.launch {
            useCase.createLocation(createRequest).collectLatest {
                it.onError {

                }
                it.onSuccess {
                    addLocationResponse.tryEmit(it)
                }

            }
        }
    }

    override val addLocationResponse = flow<LocationCreateResponse>()

    override fun locationDetail(id: Int) {
        viewModelScope.launch {
            useCase.getLocationDetail(id).collectLatest {
                it.onError {

                }
                it.onSuccess {
                    locationDetail.tryEmit(it)
                }
            }
        }
    }

    override val locationDetail = flow<LocationDetailResponse>()

    override fun postLocationIdActive(id: Int, createRequest: LocationCreateRequest) {
        viewModelScope.launch {
            useCase.postActiveLocation(id, createRequest).collectLatest {
                it.onSuccess {
                    postLocationActiveResponse.emit(it)
                }
                it.onError {
                    // log or show error
                }
            }
        }
    }

    override val postLocationActiveResponse = MutableSharedFlow<LocationActiveResponse>()

}