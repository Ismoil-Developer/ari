package uz.mrx.arigo.presentation.direction.order.impl

import uz.mrx.arigo.presentation.direction.order.OrderRetryUpdateScreenDirection
import uz.mrx.arigo.presentation.navigation.Navigator
import uz.mrx.arigo.presentation.ui.screen.fragment.order.OrderRetryUpdateScreenDirections
import javax.inject.Inject

class OrderRetryUpdateScreenDirectionImpl @Inject constructor(private val navigator: Navigator):OrderRetryUpdateScreenDirection{

    override suspend fun openAddLocationScreen(id: Int) {
        navigator.navigateTo(OrderRetryUpdateScreenDirections.actionOrderRetryUpdateScreenToAddLocationScreen(id))
    }

    override suspend fun openCancelScreenDirection(id: Int) {
        navigator
    }
}