package uz.mrx.arigo.presentation.ui.viewmodel.order.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.data.remote.response.order.AssignedResponse
import uz.mrx.arigo.data.remote.response.order.OrderPendingSearchResponse
import uz.mrx.arigo.domain.usecase.order.OrderUseCase
import uz.mrx.arigo.presentation.direction.main.MainScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.order.OrderPageViewModel
import uz.mrx.arigo.utils.flow
import javax.inject.Inject

@HiltViewModel
class OrderPageViewModelImpl @Inject constructor(private val direction:MainScreenDirection, private val useCase: OrderUseCase):OrderPageViewModel, ViewModel(){

    override val assignedResponse = flow<List<AssignedResponse>>()

    init {
        viewModelScope.launch {
            useCase.getAssignedOrder().collectLatest {
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

    override val orderSearching = flow<List<OrderPendingSearchResponse>>()

    override val orderPending = flow<List<OrderPendingSearchResponse>>()


    init {

        viewModelScope.launch {
            useCase.getOrderSearch().collectLatest {

                it.onSuccess {
                    orderSearching.tryEmit(it)
                }
                it.onError {

                }

            }
        }
    }

    init {

        viewModelScope.launch {
            useCase.getOrderPending().collectLatest {

                it.onSuccess {
                    orderPending.tryEmit(it)
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




}