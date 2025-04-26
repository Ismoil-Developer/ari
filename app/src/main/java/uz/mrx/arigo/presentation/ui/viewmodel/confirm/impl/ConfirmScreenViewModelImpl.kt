package uz.mrx.arigo.presentation.ui.viewmodel.confirm.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.data.remote.request.register.ConfirmRequest
import uz.mrx.arigo.data.remote.response.register.ConfirmResponse
import uz.mrx.arigo.domain.usecase.register.RegisterUseCase
import uz.mrx.arigo.presentation.direction.confirm.ConfirmScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.confirm.ConfirmScreenViewModel
import uz.mrx.arigo.utils.flow
import javax.inject.Inject

@HiltViewModel
class ConfirmScreenViewModelImpl @Inject constructor(
    private val direction: ConfirmScreenDirection,
    private val useCase: RegisterUseCase
) : ConfirmScreenViewModel, ViewModel() {

    override fun openScreen() {
        viewModelScope.launch {
            direction.openMainScreen()
        }
    }

    override fun postConfirm(request: ConfirmRequest) {
        viewModelScope.launch {
            useCase.confirm(request).collectLatest {

                it.onError {

                }
                it.onSuccess {
                    confirmResponse.tryEmit(it)
                }

            }
        }
    }

    override val confirmResponse = flow<ConfirmResponse>()

}