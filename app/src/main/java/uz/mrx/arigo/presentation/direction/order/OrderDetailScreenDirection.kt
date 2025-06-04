package uz.mrx.arigo.presentation.direction.order

interface OrderDetailScreenDirection {


    suspend fun openOrderUpdateRetryScreen(id:Int)

    suspend fun openCancelScreen(id: Int)

}