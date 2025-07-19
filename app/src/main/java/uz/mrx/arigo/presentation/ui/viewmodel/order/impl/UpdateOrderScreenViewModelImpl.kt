package uz.mrx.arigo.presentation.ui.viewmodel.order.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.data.remote.request.order.UpdateOrderRequest
import uz.mrx.arigo.data.remote.response.location.ActiveAddressResponse
import uz.mrx.arigo.data.remote.response.order.OrderResponse
import uz.mrx.arigo.domain.usecase.location.LocationUseCase
import uz.mrx.arigo.domain.usecase.order.OrderUseCase
import uz.mrx.arigo.presentation.direction.order.UpdateOrderScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.order.UpdateOrderScreenViewModel
import uz.mrx.arigo.utils.flow
import javax.inject.Inject

@HiltViewModel
class UpdateOrderScreenViewModelImpl @Inject constructor(private val useCaseLoc:LocationUseCase, private val useCase: OrderUseCase, private val direction:UpdateOrderScreenDirection):UpdateOrderScreenViewModel, ViewModel(){

    override fun openSearchScreenViewModel() {

        viewModelScope.launch {
            direction.openSearchDeliveryScreen()
        }

    }

    override val getActiveAddress = flow<ActiveAddressResponse>()


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


    override fun updateOrder(id: Int, request: UpdateOrderRequest) {
        viewModelScope.launch {
            useCase.updateOrder(id, request).collectLatest {
                it.onError {

                }
                it.onSuccess {
                    updateResponse.tryEmit(it)
                }
            }
        }
    }

    override val updateResponse = flow<OrderResponse>()

    override fun openAddLocationScreen(id: Int, location:String, orderId:Int) {
        viewModelScope.launch {
            direction.openAddLocationScreen(id, location, orderId)
        }
    }
}