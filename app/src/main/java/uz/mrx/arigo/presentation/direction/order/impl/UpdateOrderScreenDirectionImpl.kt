package uz.mrx.arigo.presentation.direction.order.impl

import uz.mrx.arigo.presentation.direction.order.UpdateOrderScreenDirection
import uz.mrx.arigo.presentation.navigation.Navigator
import uz.mrx.arigo.presentation.ui.screen.fragment.order.OrderUpdateScreenDirections
import javax.inject.Inject

class UpdateOrderScreenDirectionImpl @Inject constructor(private val navigator: Navigator):UpdateOrderScreenDirection {

    override suspend fun openSearchDeliveryScreen() {
        navigator.navigateTo(OrderUpdateScreenDirections.actionOrderUpdateScreenToSearchDeliveryScreen())
    }

    override suspend fun openAddLocationScreen(id:Int,location:String, orderId:Int) {
        navigator.navigateTo(OrderUpdateScreenDirections.actionOrderUpdateScreenToAddLocationScreen(id, location, orderId))
    }

}