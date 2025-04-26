package uz.mrx.arigo.presentation.direction.location.impl

import uz.mrx.arigo.presentation.direction.location.LocationScreenDirection
import uz.mrx.arigo.presentation.navigation.Navigator
import uz.mrx.arigo.presentation.ui.screen.fragment.location.LocationScreenDirections
import javax.inject.Inject

class LocationScreenDirectionImpl @Inject constructor(private val navigator: Navigator):LocationScreenDirection {
    override suspend fun openAddLocationScreen() {

        navigator.navigateTo(LocationScreenDirections.actionLocationScreenToAddLocationScreen())

    }

    override suspend fun openMainScreen() {
        navigator.navigateTo(LocationScreenDirections.actionLocationScreenToScreenMain())
    }
}