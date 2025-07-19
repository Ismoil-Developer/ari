package uz.mrx.arigo.presentation.direction.order

interface UpdateOrderScreenDirection {

    suspend fun openSearchDeliveryScreen()

    suspend fun openAddLocationScreen(id:Int, location:String, orderId:Int)

}