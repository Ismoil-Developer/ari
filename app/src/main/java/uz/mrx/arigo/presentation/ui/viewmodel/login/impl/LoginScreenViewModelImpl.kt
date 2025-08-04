package uz.mrx.arigo.presentation.ui.viewmodel.login.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.data.remote.request.register.RegisterRequest
import uz.mrx.arigo.data.remote.response.register.RegisterResponse
import uz.mrx.arigo.domain.usecase.register.RegisterUseCase
import uz.mrx.arigo.presentation.direction.login.LoginScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.login.LoginScreenViewModel
import uz.mrx.arigo.utils.flow
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModelImpl @Inject constructor(
    private val direction: LoginScreenDirection,
    private val useCase: RegisterUseCase
) : LoginScreenViewModel, ViewModel() {

    override fun openConfirmScreen(phoneNumber:String, code:String) {
        viewModelScope.launch {
            direction.openConfirmScreen(phoneNumber, code)
        }
    }

    override fun openRegisterScreen() {
        viewModelScope.launch {
            direction.openRegisterScreen()
        }
    }

    private val _toastMessage = MutableSharedFlow<String>(replay = 1)
    override val toastMessage: Flow<String> get() = _toastMessage

    private val _isLoading = MutableStateFlow(false)
    override val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    override fun postRegister(request: RegisterRequest) {
        viewModelScope.launch {
            if (_isLoading.value) return@launch // ❗ Bosilgan bo‘lsa yana bosilmasin

            _isLoading.emit(true)
            try {
                useCase.register(request).collectLatest { result ->
                    result.onSuccess {
                        registerResponse.emit(it)
                    }
                    result.onMessage {
                        _toastMessage.emit(it.toString() ?: "")
                    }
                    result.onError {
                        _toastMessage.emit(it.message ?: "Xatolik")
                    }
                }
            } finally {
                _isLoading.emit(false)
            }
        }
    }

    override val registerResponse = flow<RegisterResponse>()

}