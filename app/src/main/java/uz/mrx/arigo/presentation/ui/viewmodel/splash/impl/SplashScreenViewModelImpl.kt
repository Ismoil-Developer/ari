package uz.mrx.arigo.presentation.ui.viewmodel.splash.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.mrx.arigo.presentation.direction.splash.SplashScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.splash.SplashScreenViewModel
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModelImpl @Inject constructor(
    private val direction: SplashScreenDirection
) : SplashScreenViewModel, ViewModel() {

    override fun openLanguageScreen() {
        viewModelScope.launch {
            delay(2000)
            direction.openLanguageScreen()
        }
    }

    override fun openMainScreen() {
        viewModelScope.launch {
            delay(2000)
            direction.openMainScreen()
        }
    }

}
