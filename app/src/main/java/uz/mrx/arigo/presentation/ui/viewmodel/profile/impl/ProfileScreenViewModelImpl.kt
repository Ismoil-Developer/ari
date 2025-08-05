package uz.mrx.arigo.presentation.ui.viewmodel.profile.impl

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.data.remote.request.profile.ProfileRequest
import uz.mrx.arigo.data.remote.request.profile.ProfileRequestPhoto
import uz.mrx.arigo.data.remote.response.profile.ContactResponse
import uz.mrx.arigo.data.remote.response.profile.ProfileResponse
import uz.mrx.arigo.domain.usecase.profile.ProfileUseCase
import uz.mrx.arigo.presentation.direction.main.MainScreenDirection
import uz.mrx.arigo.presentation.direction.profile.ProfileScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.profile.ProfileScreenViewModel
import uz.mrx.arigo.utils.flow
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModelImpl @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    private val direction:MainScreenDirection,
    private val profileDirection:ProfileScreenDirection
) : ViewModel(), ProfileScreenViewModel {

    override val profileResponse = flow<ProfileResponse>()
    override val profilePhotoResponse = flow<ProfileResponse>()

    init {
        viewModelScope.launch {
            profileUseCase.getProfile().collectLatest {
                it.onSuccess {
                    profileResponse.tryEmit(it)
                }
                it.onError {
                    Log.d("ProfileResponse", "ProfileResponse: ${it.message}")
                }
            }
        }
    }

    override val putProfile = flow<ProfileResponse>()

    override fun putProfile(profileRequest: ProfileRequest) {

        viewModelScope.launch {
            profileUseCase.putProfile(
                profileRequest.full_name,
                profileRequest.phone_number
            ).collectLatest { result ->
                result.onSuccess {
                    putProfile.tryEmit(it)
                    Log.d("PROFILEIMAGE", "putProfile success: $it")
                }
                result.onError {
                    Log.e("PROFILEIMAGE", "putProfile failed: ${it.message}")
                }
            }
        }

    }

    override fun putProfileImage(profileRequestPhoto: ProfileRequestPhoto) {
        viewModelScope.launch {
            profileUseCase.putProfilePhoto(profileRequestPhoto.avatar!!).collectLatest {
                it.onSuccess {
                    profilePhotoResponse.tryEmit(it)
                }
                it.onError {

                }

            }
        }
    }

    override val getContact = flow<ContactResponse>()

    init {
        viewModelScope.launch {
            profileUseCase.getContact().collectLatest {
                it.onSuccess {
                    getContact.tryEmit(it)
                }
                it.onError {

                }
            }
        }
    }

    override fun openMainScreen() {
        viewModelScope.launch {
            profileDirection.openMainScreen()
        }
    }

    override fun openProfileScreen() {
        viewModelScope.launch {
            direction.openProfileInfoScreen()
        }
    }

    override fun openChatScreen() {
        viewModelScope.launch {
            direction.openChatScreen()
        }
    }

    override fun openLoginScreen() {
        viewModelScope.launch {
            direction.openLoginScreen()
        }
    }

}