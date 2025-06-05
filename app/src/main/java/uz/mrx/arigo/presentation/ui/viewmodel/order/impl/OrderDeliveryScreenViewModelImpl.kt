package uz.mrx.arigo.presentation.ui.viewmodel.order.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.data.remote.response.order.ActiveOrderResponse
import uz.mrx.arigo.domain.usecase.order.OrderUseCase
import uz.mrx.arigo.presentation.direction.order.OrderDeliveryScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.order.OrderDeliveryScreenViewModel
import uz.mrx.arigo.utils.flow
import javax.inject.Inject

@HiltViewModel
class OrderDeliveryScreenViewModelImpl @Inject constructor(private val direction:OrderDeliveryScreenDirection, private val orderUseCase: OrderUseCase):OrderDeliveryScreenViewModel, ViewModel() {

    override fun openChatScreen() {
        viewModelScope.launch {
            direction.openChatScreen()
        }
    }

    override fun openFindDeliveryScreen() {
        viewModelScope.launch {
            direction.openFindDeliveryScreen()
        }
    }

    override val activeOrderResponse = flow<ActiveOrderResponse>()

    init {
        viewModelScope.launch {
            orderUseCase.getActiveOrder().collectLatest {
                it.onSuccess {
                    activeOrderResponse.tryEmit(it)
                }
                it.onError {
                }
            }
        }
    }

}