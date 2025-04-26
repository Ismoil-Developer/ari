package uz.mrx.arigo.presentation.direction.intro.impl

import uz.mrx.arigo.presentation.direction.intro.IntroScreenDirection
import uz.mrx.arigo.presentation.navigation.Navigator
import uz.mrx.arigo.presentation.ui.screen.fragment.intro.IntroScreenDirections
import javax.inject.Inject

class IntroScreenDirectionImpl @Inject constructor(
    private val navigator: Navigator
) : IntroScreenDirection {

    override suspend fun openRegisterScreen() {
        navigator.navigateTo(IntroScreenDirections.actionIntroScreenToRegisterScreen())
    }

    override suspend fun openLoginScreen() {
        navigator.navigateTo(IntroScreenDirections.actionIntroScreenToLoginScreen())
    }

}
