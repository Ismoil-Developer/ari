package uz.mrx.arigo.presentation.ui.viewmodel.order.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.data.remote.request.order.OrderCancelRequest
import uz.mrx.arigo.data.remote.request.order.UpdateOrderRetryRequest
import uz.mrx.arigo.data.remote.response.order.OrderCancelResponse
import uz.mrx.arigo.data.remote.response.order.OrderDetailResponse
import uz.mrx.arigo.data.remote.response.order.UpdateOrderRetryResponse
import uz.mrx.arigo.domain.usecase.order.OrderUseCase
import uz.mrx.arigo.presentation.direction.order.OrderDetailScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.order.OrderDetailScreenViewModel
import uz.mrx.arigo.utils.flow
import javax.inject.Inject

@HiltViewModel
class OrderDetailScreenViewModelImpl @Inject constructor(private val direction:OrderDetailScreenDirection, private val orderUseCase: OrderUseCase):OrderDetailScreenViewModel, ViewModel() {


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

    override fun openOrderUpdateRetryScreen(id: Int) {
        viewModelScope.launch {
            direction.openOrderUpdateRetryScreen(id)
        }
    }

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


    override fun openCancelScreen(id: Int) {
        viewModelScope.launch {
            direction.openCancelScreen(id)
        }
    }

    override fun cancelOrder(id: Int, request: OrderCancelRequest) {
        viewModelScope.launch {
            orderUseCase.cancelOrder(id, request).collectLatest {
                it.onError {

                }
                it.onSuccess {
                    cancelResponse.tryEmit(it)
                }
            }
        }
    }

    override val cancelResponse = flow<OrderCancelResponse>()

    override fun openSearchDeliveryScreen() {
        viewModelScope.launch {
            direction.openSearchDeliveryScreen()
        }
    }

}