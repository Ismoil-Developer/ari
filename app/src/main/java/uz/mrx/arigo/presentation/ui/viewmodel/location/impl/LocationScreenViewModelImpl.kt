package uz.mrx.arigo.presentation.ui.viewmodel.location.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.data.remote.response.location.LocationActiveResponse
import uz.mrx.arigo.data.remote.response.location.LocationCreateResponse
import uz.mrx.arigo.domain.usecase.location.LocationUseCase
import uz.mrx.arigo.presentation.direction.location.LocationScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.location.LocationScreenViewModel
import uz.mrx.arigo.utils.flow
import javax.inject.Inject


@HiltViewModel
class LocationScreenViewModelImpl @Inject constructor(private val direction: LocationScreenDirection, private val useCase: LocationUseCase):LocationScreenViewModel, ViewModel() {

    override fun openAddLocationScreen() {
        viewModelScope.launch {
            direction.openAddLocationScreen()
        }
    }

    override val getLocation = flow<List<LocationCreateResponse>>()
    override fun postLocationIdActive(id: Int) {
        viewModelScope.launch {
            useCase.postActiveLocation(id).collectLatest {
                it.onSuccess {
                    postLocationActiveResponse.tryEmit(it)
                }
                it.onError {

                }
            }
        }
    }

    override val postLocationActiveResponse = flow<LocationActiveResponse>()


    init {
        viewModelScope.launch {
            useCase.getLocation().collectLatest {
                it.onSuccess {
                    getLocation.tryEmit(it)
                }
                it.onError {

                }
            }
        }
    }

    override fun openMainScreen() {
        viewModelScope.launch {
            direction.openMainScreen()
        }
    }

}