package uz.mrx.arigo.presentation.direction.order

interface UpdateOrderScreenDirection {

    suspend fun openSearchDeliveryScreen(id: Int)

    suspend fun openAddLocationScreen(id:Int, location:String, orderId:Int)

}