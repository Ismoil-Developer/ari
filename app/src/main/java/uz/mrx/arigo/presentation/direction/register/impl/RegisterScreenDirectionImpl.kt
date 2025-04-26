package uz.mrx.arigo.presentation.direction.register.impl

import uz.mrx.arigo.presentation.direction.register.RegisterScreenDirection
import uz.mrx.arigo.presentation.navigation.Navigator
import uz.mrx.arigo.presentation.ui.screen.fragment.auth.register.RegisterScreenDirections
import javax.inject.Inject

class RegisterScreenDirectionImpl @Inject constructor(private val navigator: Navigator):
    RegisterScreenDirection {

    override suspend fun openConfirmScreen(phoneNumber:String, code:String) {
        navigator.navigateTo(RegisterScreenDirections.actionRegisterScreenToConfirmScreen(phoneNumber, code))
    }

    override suspend fun openLoginScreen(){
        navigator.navigateTo(RegisterScreenDirections.actionRegisterScreenToLoginScreen())
    }

}