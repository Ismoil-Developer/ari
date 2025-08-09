package uz.mrx.arigo.presentation.ui.viewmodel.homepage.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.data.remote.request.location.LocationCreateRequest
import uz.mrx.arigo.data.remote.response.feature.RoleResponse
import uz.mrx.arigo.data.remote.response.feature.advertising.AdvertisingResponse
import uz.mrx.arigo.data.remote.response.location.ActiveAddressResponse
import uz.mrx.arigo.data.remote.response.location.LocationCreateResponse
import uz.mrx.arigo.data.remote.response.order.ActiveOrderResponse
import uz.mrx.arigo.data.remote.response.order.AssignedResponse
import uz.mrx.arigo.data.remote.response.order.OrderPendingSearchResponse
import uz.mrx.arigo.domain.usecase.feature.FeatureUseCase
import uz.mrx.arigo.domain.usecase.location.LocationUseCase
import uz.mrx.arigo.domain.usecase.order.OrderUseCase
import uz.mrx.arigo.presentation.direction.main.MainScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.homepage.HomePageViewModel
import uz.mrx.arigo.utils.flow
import javax.inject.Inject

@HiltViewModel
class HomePageViewModelImpl @Inject constructor(
    private val direction: MainScreenDirection,
    private val useCase: FeatureUseCase,
    private val useCaseLoc: LocationUseCase,
    private val orderUseCase: OrderUseCase
) : HomePageViewModel, ViewModel() {

    override fun openMagazineDetailScreen(id: Int, roleId: Int) {
        viewModelScope.launch {
            direction.openMagazineDetailScreen(id, roleId)
        }
    }

    override fun openLocationScreen() {
        viewModelScope.launch {
            direction.openLocationScreen()
        }
    }

    override fun openShopListScreen(id: Int) {
        viewModelScope.launch {
            direction.openShopListScreen(id)
        }
    }

    override val featureResponse = flow<List<RoleResponse>>()

    override val getActiveAddress = flow<ActiveAddressResponse>()

    override val getAdvertisingResponse = flow<List<AdvertisingResponse>>()

    init {
        viewModelScope.launch {
            useCase.getAdvertising().collectLatest {
                it.onSuccess {
                    getAdvertisingResponse.tryEmit(it)
                }
                it.onError {

                }
            }
        }
    }

    init {
        viewModelScope.launch {
            useCaseLoc.getActiveAddress().collectLatest {
                it.onSuccess {
                    getActiveAddress.tryEmit(it)
                }
                it.onError {

                }
            }
        }
    }

    override fun openNotification() {

        viewModelScope.launch {
            direction.openNotification()
        }

    }

    init {
        viewModelScope.launch {
            useCase.getFeatures().collectLatest {
                it.onSuccess {
                    featureResponse.tryEmit(it)
                }
                it.onError {

                }
            }
        }
    }

    override val getPendingSearchResponse = flow<List<OrderPendingSearchResponse>>()

    init {
        viewModelScope.launch {
            orderUseCase.getOrderSearch().collectLatest {

                it.onSuccess {
                    getPendingSearchResponse.tryEmit(it)
                }

                it.onError {

                }
            }
        }
    }

    override fun openOrderDetailScreen(id: Int) {
        viewModelScope.launch {
            direction.openOrderDetailScreen(id)
        }
    }

    override val activeOrderResponse = flow<ActiveOrderResponse>()


    override fun getActiveOrder(id: Int) {
        viewModelScope.launch {
            orderUseCase.getActiveOrder(id).collectLatest {
                it.onSuccess {
                    activeOrderResponse.tryEmit(it)
                }
                it.onError {
                }
            }
        }
    }

    override val assignedResponse = flow<List<AssignedResponse>>()

    init {
        viewModelScope.launch {
            orderUseCase.getAssignedOrder().collectLatest {
                it.onSuccess {
                    assignedResponse.tryEmit(it)
                }
                it.onError {

                }
            }
        }
    }

    override fun openOrderDeliveryScreen(id: Int) {
        viewModelScope.launch {
            direction.openOrderDeliveryScreen("", id)
        }
    }

    override fun addLocation(createRequest: LocationCreateRequest) {
        viewModelScope.launch {
            useCaseLoc.createLocation(createRequest).collectLatest {
                it.onError {

                }
                it.onSuccess {
                    addLocationResponse.tryEmit(it)
                }

            }
        }
    }

    override val addLocationResponse = flow<LocationCreateResponse>()


}