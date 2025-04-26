package uz.mrx.arigo.presentation.direction.login

interface LoginScreenDirection {

    suspend fun openConfirmScreen(phoneNumber:String, code:String)
    suspend fun openRegisterScreen()

}