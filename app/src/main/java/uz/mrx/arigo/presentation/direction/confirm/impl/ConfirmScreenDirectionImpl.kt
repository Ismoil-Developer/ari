package uz.mrx.arigo.presentation.direction.confirm.impl

import uz.mrx.arigo.presentation.direction.confirm.ConfirmScreenDirection
import uz.mrx.arigo.presentation.navigation.Navigator
import uz.mrx.arigo.presentation.ui.screen.fragment.auth.confirm.ConfirmScreenDirections
import javax.inject.Inject

class ConfirmScreenDirectionImpl @Inject constructor(
    private val navigator: Navigator
) : ConfirmScreenDirection {

    override suspend fun openMainScreen() {
        navigator.navigateTo(ConfirmScreenDirections.actionConfirmScreenToScreenMain())
    }

}