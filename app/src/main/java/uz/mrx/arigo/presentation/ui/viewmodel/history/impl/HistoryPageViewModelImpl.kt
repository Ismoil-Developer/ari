package uz.mrx.arigo.presentation.ui.viewmodel.history.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.data.remote.response.history.OrderHistoryResponse
import uz.mrx.arigo.domain.usecase.order.OrderUseCase
import uz.mrx.arigo.presentation.direction.main.MainScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.history.HistoryPageViewModel
import uz.mrx.arigo.utils.flow
import javax.inject.Inject

@HiltViewModel
class HistoryPageViewModelImpl @Inject constructor(private val direction: MainScreenDirection, private val orderUseCase: OrderUseCase):HistoryPageViewModel, ViewModel()  {

    override fun openHistoryDetailScreen() {
        viewModelScope.launch {
            direction.openHistoryDetailScreen()
        }
    }

    override val getHistory = flow<List<OrderHistoryResponse>>()

    init {
        viewModelScope.launch {
            orderUseCase.getOrderHistory().collectLatest {
                it.onSuccess {
                    getHistory.tryEmit(it)
                }
                it.onError {

                }
            }
        }
    }

}