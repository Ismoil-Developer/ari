package uz.mrx.arigo.presentation.ui.viewmodel.delivery.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.mrx.arigo.presentation.direction.delivery.FindDeliveryScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.delivery.FindDeliveryScreenViewModel
import javax.inject.Inject

@HiltViewModel
class FindDeliveryScreenViewModelImpl @Inject constructor(private val direction: FindDeliveryScreenDirection):FindDeliveryScreenViewModel, ViewModel(){
    override fun openOrderCompletedScreen() {
        viewModelScope.launch {
            direction.openOrderCompleted()
        }
    }
}