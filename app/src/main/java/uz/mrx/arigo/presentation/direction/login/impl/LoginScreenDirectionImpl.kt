package uz.mrx.arigo.presentation.direction.login.impl

import uz.mrx.arigo.presentation.direction.login.LoginScreenDirection
import uz.mrx.arigo.presentation.navigation.Navigator
import uz.mrx.arigo.presentation.ui.screen.fragment.auth.login.LoginScreenDirections
import javax.inject.Inject

class LoginScreenDirectionImpl @Inject constructor(private val navigator: Navigator) :
    LoginScreenDirection {
    override suspend fun openConfirmScreen(phoneNumber:String, code:String) {
        navigator.navigateTo(LoginScreenDirections.actionLoginScreenToConfirmScreen(phoneNumber, code))
    }

    override suspend fun openRegisterScreen() {
    }

}