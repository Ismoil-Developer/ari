package uz.mrx.arigo.presentation.direction.order

interface OrderRetryUpdateScreenDirection {

    suspend fun openAddLocationScreen(id:Int, location:String, orderId:Int)

    suspend fun openCancelScreenDirection(id: Int)

    suspend fun openSearchDeliveryScreen()


}