package uz.mrx.arigo.presentation.direction.main.impl

import uz.mrx.arigo.data.remote.response.order.Coordinates
import uz.mrx.arigo.presentation.direction.main.MainScreenDirection
import uz.mrx.arigo.presentation.navigation.Navigator
import uz.mrx.arigo.presentation.ui.screen.fragment.main.MainScreenDirections
import javax.inject.Inject

class MainScreenDirectionImpl @Inject constructor(private val navigator: Navigator) :
    MainScreenDirection{

    override suspend fun openMagazineDetailScreen(id:Int) {
        navigator.navigateTo(MainScreenDirections.actionScreenMainToMagazineDetailScreen(id))
    }

    override suspend fun openLocationScreen() {
        navigator.navigateTo(MainScreenDirections.actionScreenMainToLocationScreen())
    }

    override suspend fun openShopListScreen(id: Int) {
        navigator.navigateTo(MainScreenDirections.actionScreenMainToShopListScreen(id))
    }

    override suspend fun openProfileInfoScreen() {
        navigator.navigateTo(MainScreenDirections.actionScreenMainToProfileInfoScreen())
    }

    override suspend fun openNotification() {
        navigator.navigateTo(MainScreenDirections.actionScreenMainToNotificationScreen())
    }

    override suspend fun openHistoryDetailScreen() {
        navigator.navigateTo(MainScreenDirections.actionScreenMainToHistoryDetailScreen())
    }

    override suspend fun openChatScreen() {
        navigator.navigateTo(MainScreenDirections.actionScreenMainToChatScreen())
    }

    override suspend fun openOrderDetailScreen(id: Int) {
        navigator.navigateTo(MainScreenDirections.actionScreenMainToOrderDetailScreen(id))
    }

    override suspend fun openOrderDeliveryScreen(coordinates: String, id: Int) {
        navigator.navigateTo(MainScreenDirections.actionScreenMainToOrderDeliveryScreen(coordinates, id))
    }

}