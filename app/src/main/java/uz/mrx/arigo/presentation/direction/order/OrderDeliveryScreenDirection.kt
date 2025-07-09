package uz.mrx.arigo.presentation.direction.order

interface OrderDeliveryScreenDirection {

    suspend fun openChatScreen()

    suspend fun openFindDeliveryScreen()

    suspend fun openOrderCompletedScreen(id: Int)

    suspend fun openOrderDetail(id:Int)

    suspend fun openOrderCancelScreen(id: Int)

    suspend fun openMainScreen()

}