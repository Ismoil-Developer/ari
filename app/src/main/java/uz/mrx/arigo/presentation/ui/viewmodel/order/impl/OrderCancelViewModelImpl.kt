package uz.mrx.arigo.presentation.ui.viewmodel.order.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.data.remote.request.order.OrderCancelRequest
import uz.mrx.arigo.data.remote.response.order.OrderCancelResponse
import uz.mrx.arigo.domain.usecase.order.OrderUseCase
import uz.mrx.arigo.presentation.ui.viewmodel.order.OrderCancelViewModel
import uz.mrx.arigo.utils.flow
import javax.inject.Inject

@HiltViewModel
class OrderCancelViewModelImpl  @Inject constructor(private val useCase: OrderUseCase):OrderCancelViewModel, ViewModel(){


    override fun cancelOrder(id: Int, request: OrderCancelRequest) {
        viewModelScope.launch {
            useCase.cancelOrder(id, request).collectLatest {
                it.onError {

                }
                it.onSuccess {
                    cancelResponse.tryEmit(it)
                }
            }
        }
    }

    override val cancelResponse = flow<OrderCancelResponse>()
}