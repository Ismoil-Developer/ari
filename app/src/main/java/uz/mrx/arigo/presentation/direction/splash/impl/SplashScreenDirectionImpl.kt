package uz.mrx.arigo.presentation.direction.splash.impl

import uz.mrx.arigo.presentation.direction.splash.SplashScreenDirection
import uz.mrx.arigo.presentation.navigation.Navigator
import uz.mrx.arigo.presentation.ui.screen.fragment.splash.SplashScreenDirections
import javax.inject.Inject


class SplashScreenDirectionImpl @Inject constructor(
    private val navigator: Navigator
) : SplashScreenDirection {

    override suspend fun openLanguageScreen() =
        navigator.navigateTo(SplashScreenDirections.actionSplashScreenToLanguageScreen())

    override suspend fun openMainScreen() {
        navigator.navigateTo(SplashScreenDirections.actionSplashScreenToScreenMain())
    }

}