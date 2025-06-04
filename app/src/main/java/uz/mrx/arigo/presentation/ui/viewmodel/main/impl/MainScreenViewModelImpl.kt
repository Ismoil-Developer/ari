package uz.mrx.arigo.presentation.ui.viewmodel.main.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.mrx.arigo.presentation.direction.main.MainScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.main.MainScreenViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModelImpl @Inject constructor(private val direction: MainScreenDirection):MainScreenViewModel, ViewModel(){
    override fun openOrderDeliveryScreen(coordinates: String) {
        viewModelScope.launch {
            direction.openOrderDeliveryScreen(coordinates)
        }
    }
}