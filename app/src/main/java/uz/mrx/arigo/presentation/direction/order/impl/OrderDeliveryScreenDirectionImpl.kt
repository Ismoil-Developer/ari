package uz.mrx.arigo.presentation.direction.order.impl

import uz.mrx.arigo.presentation.direction.order.OrderDeliveryScreenDirection
import uz.mrx.arigo.presentation.navigation.Navigator
import uz.mrx.arigo.presentation.ui.screen.fragment.order.OrderDeliveryScreenDirections
import javax.inject.Inject

class OrderDeliveryScreenDirectionImpl @Inject constructor(private val navigator: Navigator):OrderDeliveryScreenDirection {

    override suspend fun openChatScreen() {
        navigator.navigateTo(OrderDeliveryScreenDirections.actionOrderDeliveryScreenToChatScreen())
    }

    override suspend fun openFindDeliveryScreen() {
        navigator.navigateTo(OrderDeliveryScreenDirections.actionOrderDeliveryScreenToFindDeliveryScreen())
    }

    override suspend fun openOrderCompletedScreen() {
        navigator.navigateTo(OrderDeliveryScreenDirections.actionOrderDeliveryScreenToOrderCompletedScreen())
    }

}