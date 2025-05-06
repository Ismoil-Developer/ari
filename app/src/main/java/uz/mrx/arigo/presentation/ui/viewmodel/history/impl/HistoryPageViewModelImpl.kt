package uz.mrx.arigo.presentation.ui.viewmodel.history.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.mrx.arigo.presentation.direction.main.MainScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.history.HistoryPageViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryPageViewModelImpl @Inject constructor(private val direction: MainScreenDirection):HistoryPageViewModel, ViewModel()  {

    override fun openHistoryDetailScreen() {
        viewModelScope.launch {
            direction.openHistoryDetailScreen()
        }
    }

}