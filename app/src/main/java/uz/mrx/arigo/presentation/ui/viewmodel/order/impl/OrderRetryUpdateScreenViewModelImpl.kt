package uz.mrx.arigo.presentation.ui.viewmodel.order.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.data.remote.request.order.UpdateOrderRetryRequest
import uz.mrx.arigo.data.remote.response.order.OrderDetailResponse
import uz.mrx.arigo.data.remote.response.order.UpdateOrderRetryResponse
import uz.mrx.arigo.domain.usecase.order.OrderUseCase
import uz.mrx.arigo.presentation.direction.order.OrderRetryUpdateScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.order.OrderRetryUpdateScreenViewModel
import uz.mrx.arigo.utils.flow
import javax.inject.Inject

@HiltViewModel
class OrderRetryUpdateScreenViewModelImpl @Inject constructor(private val orderUseCase: OrderUseCase, val direction: OrderRetryUpdateScreenDirection):OrderRetryUpdateScreenViewModel, ViewModel() {

    override fun updateOrderRetry(id: Int, request: UpdateOrderRetryRequest) {

        viewModelScope.launch {
            orderUseCase.updateOrderRetry(id, request).collectLatest {
                it.onSuccess {
                    updateOrderRetryResponse.tryEmit(it)
                }
                it.onError {

                }
            }
        }

    }

    override val updateOrderRetryResponse = flow<UpdateOrderRetryResponse>()


    override fun getOrderDetail(id: Int) {
        viewModelScope.launch {
            orderUseCase.getOrderDetail(id).collectLatest {
                it.onError {

                }

                it.onSuccess {
                    getOrderDetailResponse.tryEmit(it)
                }
            }
        }
    }

    override val getOrderDetailResponse = flow<OrderDetailResponse>()


    override fun openAddLocationScreen(id: Int, location:String, orderId:Int) {
        viewModelScope.launch {
            direction.openAddLocationScreen(id, location, orderId)
        }
    }

    override fun openCancelScreen(id: Int) {
        viewModelScope.launch {
            direction.openCancelScreenDirection(id)
        }
    }

    override fun openSearchDeliveryScreen() {
        viewModelScope.launch {
            direction.openSearchDeliveryScreen()
        }
    }

}