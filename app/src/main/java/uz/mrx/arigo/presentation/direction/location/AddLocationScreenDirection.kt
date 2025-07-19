package uz.mrx.arigo.presentation.direction.location

interface AddLocationScreenDirection {

    suspend fun openLocationScreen()

    suspend fun openUpdateScreen(id:Int, orderId:Int)

    suspend fun openUpdateRetryScreen(id: Int, orderId:Int)

}