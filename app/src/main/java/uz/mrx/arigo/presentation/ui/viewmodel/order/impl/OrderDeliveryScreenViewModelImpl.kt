package uz.mrx.arigo.presentation.ui.viewmodel.order.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.mrx.arigo.presentation.direction.order.OrderDeliveryScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.order.OrderDeliveryScreenViewModel
import javax.inject.Inject

@HiltViewModel
class OrderDeliveryScreenViewModelImpl @Inject constructor(private val direction:OrderDeliveryScreenDirection):OrderDeliveryScreenViewModel, ViewModel() {

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

}