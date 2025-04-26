package uz.mrx.arigo.presentation.direction.magazinedetail.impl

import uz.mrx.arigo.presentation.direction.magazinedetail.MagazineDetailScreenDirection
import uz.mrx.arigo.presentation.navigation.Navigator
import uz.mrx.arigo.presentation.ui.screen.fragment.detail.MagazineDetailScreenDirections
import javax.inject.Inject

class MagazineDetailScreenDirectionImpl @Inject constructor(private val navigator: Navigator) : MagazineDetailScreenDirection  {
    override suspend fun openSearchDeliveryScreen() {
        navigator.navigateTo(MagazineDetailScreenDirections.actionMagazineDetailScreenToSearchDeliveryScreen())
    }
}