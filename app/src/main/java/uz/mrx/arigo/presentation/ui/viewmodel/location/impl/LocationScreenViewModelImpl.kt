package uz.mrx.arigo.presentation.ui.viewmodel.location.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.data.remote.response.location.LocationActiveResponse
import uz.mrx.arigo.data.remote.response.location.LocationCreateResponse
import uz.mrx.arigo.data.remote.response.location.LocationDeleteResponse
import uz.mrx.arigo.domain.usecase.location.LocationUseCase
import uz.mrx.arigo.presentation.direction.location.LocationScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.location.LocationScreenViewModel
import uz.mrx.arigo.utils.flow
import javax.inject.Inject

@HiltViewModel
class LocationScreenViewModelImpl @Inject constructor(
    private val direction: LocationScreenDirection,
    private val useCase: LocationUseCase
) : LocationScreenViewModel, ViewModel() {

    override val getLocation = MutableStateFlow<List<LocationCreateResponse>>(emptyList())

    override val postLocationActiveResponse = MutableSharedFlow<LocationActiveResponse>()

    override val deleteLocation = MutableSharedFlow<LocationDeleteResponse>()

    init {
        getLocations() // Boshlanishida yuklash
    }

    override fun getLocations() {
        viewModelScope.launch {
            useCase.getLocation().collectLatest {
                it.onSuccess { data ->
                    getLocation.emit(data)
                }
                it.onError {
                    // log or show error
                }
            }
        }
    }

    override fun deleteLocation(id: Int) {
        viewModelScope.launch {
            useCase.deleteLocation(id).collectLatest {
                it.onSuccess { result ->
                    deleteLocation.emit(result)
                    getLocations() // MUHIM: O‘chirilgandan so‘ng yangilash
                }
                it.onError {
                    // log or show error
                }
            }
        }
    }

    override fun postLocationIdActive(id: Int) {
        viewModelScope.launch {
            useCase.postActiveLocation(id).collectLatest {
                it.onSuccess {
                    postLocationActiveResponse.emit(it)
                }
                it.onError {
                    // log or show error
                }
            }
        }
    }

    override fun openAddLocationScreen() {
        viewModelScope.launch { direction.openAddLocationScreen() }
    }

    override fun openMainScreen() {
        viewModelScope.launch { direction.openMainScreen() }
    }
}
