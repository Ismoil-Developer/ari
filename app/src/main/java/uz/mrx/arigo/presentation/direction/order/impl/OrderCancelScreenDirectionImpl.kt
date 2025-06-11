package uz.mrx.arigo.presentation.direction.order.impl

import uz.mrx.arigo.presentation.direction.order.OrderCancelScreenDirection
import uz.mrx.arigo.presentation.navigation.Navigator
import uz.mrx.arigo.presentation.ui.screen.fragment.order.OrderCancelScreenDirections
import javax.inject.Inject

class OrderCancelScreenDirectionImpl @Inject constructor(private val navigator: Navigator):OrderCancelScreenDirection {
    override suspend fun openMainScreen() {
        navigator.navigateTo(OrderCancelScreenDirections.actionOrderCancelScreenToScreenMain())
    }
}