package uz.mrx.arigo.presentation.direction.searchdelivery

interface SearchDeliveryScreenDirection {

    suspend fun openOrderDeliveryScreen(coordinates: String, id: Int)

    suspend fun openOrderDetailScreen(id:Int)

    suspend fun openCancelScreen(id: Int)

    suspend fun openMainScreen()


}