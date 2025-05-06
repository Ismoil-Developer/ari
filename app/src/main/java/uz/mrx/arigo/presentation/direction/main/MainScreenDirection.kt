package uz.mrx.arigo.presentation.direction.main

interface MainScreenDirection {

    suspend fun openMagazineDetailScreen(id:Int)

    suspend fun openLocationScreen()

    suspend fun openShopListScreen(id: Int)

    suspend fun openProfileInfoScreen()

    suspend fun openNotification()

    suspend fun openHistoryDetailScreen()

}