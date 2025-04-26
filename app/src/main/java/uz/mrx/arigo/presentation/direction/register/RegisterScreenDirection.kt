package uz.mrx.arigo.presentation.direction.register

interface RegisterScreenDirection {

    suspend fun openConfirmScreen(phoneNumber:String, code:String)

    suspend fun openLoginScreen()


}