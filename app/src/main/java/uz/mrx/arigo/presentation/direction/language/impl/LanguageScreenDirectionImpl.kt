package uz.mrx.arigo.presentation.direction.language.impl

import uz.mrx.arigo.presentation.direction.language.LanguageScreenDirection
import uz.mrx.arigo.presentation.navigation.Navigator
import uz.mrx.arigo.presentation.ui.screen.fragment.language.LanguageScreenDirections
import javax.inject.Inject

class LanguageScreenDirectionImpl @Inject constructor(
    private val navigator: Navigator
) : LanguageScreenDirection {

    override suspend fun openIntroScreen() {
        navigator.navigateTo(LanguageScreenDirections.actionLanguageScreenToIntroScreen())
    }

}