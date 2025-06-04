package uz.mrx.arigo.presentation.direction.order.impl

import uz.mrx.arigo.presentation.direction.order.OrderDetailScreenDirection
import uz.mrx.arigo.presentation.navigation.Navigator
import uz.mrx.arigo.presentation.ui.screen.fragment.detail.MagazineDetailScreenDirections
import uz.mrx.arigo.presentation.ui.screen.fragment.order.OrderDetailScreen
import uz.mrx.arigo.presentation.ui.screen.fragment.order.OrderDetailScreenDirections
import javax.inject.Inject

class OrderDetailScreenDirectionImpl @Inject constructor(private val navigator: Navigator):OrderDetailScreenDirection {

    override suspend fun openOrderUpdateRetryScreen(id:Int) {

        navigator.navigateTo(OrderDetailScreenDirections.actionOrderDetailScreenToOrderRetryUpdateScreen(id))

    }

    override suspend fun openCancelScreen(id: Int) {
        navigator.navigateTo(OrderDetailScreenDirections.actionOrderDetailScreenToOrderCancelScreen(id))
    }

}