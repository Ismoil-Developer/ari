package uz.mrx.arigo.presentation.direction.shop.impl

import uz.mrx.arigo.presentation.direction.shop.ShopListScreenDirection
import uz.mrx.arigo.presentation.navigation.Navigator
import uz.mrx.arigo.presentation.ui.screen.fragment.shop.ShopListScreenDirections
import javax.inject.Inject

class ShopListScreenDirectionImpl @Inject constructor(private val navigator: Navigator) :
    ShopListScreenDirection {

    override suspend fun openMapSearchScreen(id: Int) {
        navigator.navigateTo(ShopListScreenDirections.actionShopListScreenToSearchMapScreen(id))
    }

    override suspend fun openShopDetailScreen(id: Int) {
        navigator.navigateTo(ShopListScreenDirections.actionShopListScreenToMagazineDetailScreen(id))
    }


}