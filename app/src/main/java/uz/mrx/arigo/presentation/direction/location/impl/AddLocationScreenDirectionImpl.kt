package uz.mrx.arigo.presentation.direction.location.impl

import uz.mrx.arigo.presentation.direction.location.AddLocationScreenDirection
import uz.mrx.arigo.presentation.navigation.Navigator
import uz.mrx.arigo.presentation.ui.screen.fragment.location.AddLocationScreenDirections
import javax.inject.Inject

class AddLocationScreenDirectionImpl @Inject constructor(private val navigator: Navigator):AddLocationScreenDirection {

    override suspend fun openLocationScreen() {

        navigator.navigateTo(AddLocationScreenDirections.actionAddLocationScreenToLocationScreen())

    }

    override suspend fun openUpdateScreen(id:Int, orderId:Int) {
        navigator.navigateTo(AddLocationScreenDirections.actionAddLocationScreenToOrderUpdateScreen(id, orderId))
    }

    override suspend fun openUpdateRetryScreen(id: Int, orderId:Int) {
        navigator.navigateTo(AddLocationScreenDirections.actionAddLocationScreenToOrderRetryUpdateScreen(id, orderId))
    }

}