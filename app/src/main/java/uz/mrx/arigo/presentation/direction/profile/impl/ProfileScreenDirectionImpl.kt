package uz.mrx.arigo.presentation.direction.profile.impl

import uz.mrx.arigo.presentation.direction.profile.ProfileScreenDirection
import uz.mrx.arigo.presentation.navigation.Navigator
import uz.mrx.arigo.presentation.ui.screen.fragment.profile.ProfileInfoScreenDirections
import javax.inject.Inject

class ProfileScreenDirectionImpl @Inject constructor(private val navigator: Navigator):ProfileScreenDirection {

    override suspend fun openMainScreen() {
        navigator.navigateTo(ProfileInfoScreenDirections.actionProfileInfoScreenToScreenMain())
    }

}