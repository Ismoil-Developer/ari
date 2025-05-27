package uz.mrx.arigo.presentation.direction.delivery.impl

import uz.mrx.arigo.presentation.direction.delivery.FindDeliveryScreenDirection
import uz.mrx.arigo.presentation.navigation.Navigator
import uz.mrx.arigo.presentation.ui.screen.fragment.delivery.FindDeliveryScreenDirections
import javax.inject.Inject

class FindDeliveryScreenDirectionImpl @Inject constructor(private val navigator: Navigator):FindDeliveryScreenDirection {

    override suspend fun openOrderCompleted() {
        navigator.navigateTo(FindDeliveryScreenDirections.actionFindDeliveryScreenToOrderCompletedScreen())
    }

}