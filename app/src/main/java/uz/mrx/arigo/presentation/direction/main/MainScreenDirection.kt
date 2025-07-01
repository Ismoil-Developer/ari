package uz.mrx.arigo.presentation.direction.main

import uz.mrx.arigo.data.remote.response.order.Coordinates

interface MainScreenDirection {

    suspend fun openMagazineDetailScreen(id:Int)

    suspend fun openLocationScreen()

    suspend fun openShopListScreen(id: Int)

    suspend fun openProfileInfoScreen()

    suspend fun openNotification()

    suspend fun openHistoryDetailScreen(id: Int)

    suspend fun openChatScreen()

    suspend fun openOrderDetailScreen(id: Int)

    suspend fun openOrderDeliveryScreen(coordinates: String, id: Int)


}