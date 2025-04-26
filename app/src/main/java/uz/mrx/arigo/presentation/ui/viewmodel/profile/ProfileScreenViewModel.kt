package uz.mrx.arigo.presentation.ui.viewmodel.profile

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.request.profile.ProfileRequest
import uz.mrx.arigo.data.remote.request.profile.ProfileRequestPhoto
import uz.mrx.arigo.data.remote.response.profile.ProfileResponse

interface ProfileScreenViewModel {

    val profileResponse: Flow<ProfileResponse>

    val profilePhotoResponse: Flow<ProfileResponse>

    val putProfile: Flow<ProfileResponse>

    fun putProfile(profileRequest: ProfileRequest)

    fun putProfileImage(profileRequestPhoto: ProfileRequestPhoto)

    fun openMainScreen()

    fun openProfileScreen()

}