package uz.mrx.arigo.presentation.direction.main


interface MainScreenDirection {

    suspend fun openMagazineDetailScreen(id:Int, roleId:Int)

    suspend fun openLocationScreen()

    suspend fun openShopListScreen(id: Int)

    suspend fun openProfileInfoScreen()

    suspend fun openNotification()

    suspend fun openHistoryDetailScreen(id: Int)

    suspend fun openChatScreen()

    suspend fun openOrderDetailScreen(id: Int)

    suspend fun openOrderDeliveryScreen(coordinates: String, id: Int)

    suspend fun openLoginScreen()

}