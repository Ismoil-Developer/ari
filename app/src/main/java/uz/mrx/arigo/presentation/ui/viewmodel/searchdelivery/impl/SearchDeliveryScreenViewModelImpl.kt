package uz.mrx.arigo.presentation.ui.viewmodel.searchdelivery.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.mrx.arigo.presentation.direction.searchdelivery.SearchDeliveryScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.searchdelivery.SearchDeliveryScreenViewModel
import javax.inject.Inject

@HiltViewModel
class SearchDeliveryScreenViewModelImpl @Inject constructor(private val direction: SearchDeliveryScreenDirection):SearchDeliveryScreenViewModel, ViewModel() {
    override fun openFindDeliveryScreen() {

        viewModelScope.launch {

            direction.openFinDeliveryScreen()

        }

    }
}