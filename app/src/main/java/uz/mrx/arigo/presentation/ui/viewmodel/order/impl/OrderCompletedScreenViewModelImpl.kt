package uz.mrx.arigo.presentation.ui.viewmodel.order.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.mrx.arigo.presentation.direction.order.OrderCompletedScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.order.OrderCompletedScreenViewModel
import javax.inject.Inject

@HiltViewModel
class OrderCompletedScreenViewModelImpl @Inject constructor(private val direction: OrderCompletedScreenDirection) : OrderCompletedScreenViewModel, ViewModel() {

    override fun openOrderCompletedScreen() {
        viewModelScope.launch {
            direction.openMainScreen()
        }
    }

}