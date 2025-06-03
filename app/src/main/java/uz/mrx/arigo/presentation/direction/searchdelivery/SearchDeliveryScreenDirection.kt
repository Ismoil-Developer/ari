package uz.mrx.arigo.presentation.direction.searchdelivery

interface SearchDeliveryScreenDirection {

    suspend fun openOrderDeliveryScreen(coordinates: String)

    suspend fun openOrderDetailScreen(id:Int)

}