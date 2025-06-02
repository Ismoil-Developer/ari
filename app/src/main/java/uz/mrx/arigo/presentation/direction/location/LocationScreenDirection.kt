package uz.mrx.arigo.presentation.direction.location

interface LocationScreenDirection {

    suspend fun openAddLocationScreen(id:Int)

    suspend fun openMainScreen()

}