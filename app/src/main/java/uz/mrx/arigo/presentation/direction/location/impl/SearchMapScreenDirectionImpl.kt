package uz.mrx.arigo.presentation.direction.location.impl

import uz.mrx.arigo.presentation.direction.location.SearchMapScreenDirection
import uz.mrx.arigo.presentation.navigation.Navigator
import uz.mrx.arigo.presentation.ui.screen.fragment.shop.SearchMapScreenDirections
import javax.inject.Inject

class SearchMapScreenDirectionImpl @Inject constructor(private val navigator: Navigator):SearchMapScreenDirection{

    override suspend fun openSearchMapScreen(id: Int) {
        navigator.navigateTo(SearchMapScreenDirections.actionSearchMapScreenToMagazineDetailScreen(id))
    }

}