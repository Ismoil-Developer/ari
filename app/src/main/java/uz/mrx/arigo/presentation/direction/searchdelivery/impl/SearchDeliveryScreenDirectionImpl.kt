package uz.mrx.arigo.presentation.direction.searchdelivery.impl

import uz.mrx.arigo.presentation.direction.searchdelivery.SearchDeliveryScreenDirection
import uz.mrx.arigo.presentation.navigation.Navigator
import uz.mrx.arigo.presentation.ui.screen.fragment.order.SearchDeliveryScreenDirections
import javax.inject.Inject

class SearchDeliveryScreenDirectionImpl @Inject constructor(private val navigator: Navigator):SearchDeliveryScreenDirection  {

    override suspend fun openOrderDeliveryScreen(coordinates: String, id: Int) {
        navigator.navigateTo(SearchDeliveryScreenDirections.actionSearchDeliveryScreenToOrderDeliveryScreen(coordinates, id))
    }

    override suspend fun openOrderDetailScreen(id: Int) {
        navigator.navigateTo(SearchDeliveryScreenDirections.actionSearchDeliveryScreenToOrderDetailScreen(id))
    }

    override suspend fun openCancelScreen(id: Int) {
        navigator.navigateTo(SearchDeliveryScreenDirections.actionSearchDeliveryScreenToOrderCancelScreen(id))
    }

}