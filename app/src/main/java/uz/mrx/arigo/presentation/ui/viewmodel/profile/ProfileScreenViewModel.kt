package uz.mrx.arigo.presentation.ui.viewmodel.profile

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.request.profile.ProfileRequest
import uz.mrx.arigo.data.remote.request.profile.ProfileRequestPhoto
import uz.mrx.arigo.data.remote.response.profile.ContactResponse
import uz.mrx.arigo.data.remote.response.profile.ProfileResponse
import uz.mrx.arigo.utils.ResultData

interface ProfileScreenViewModel {

    val profileResponse: Flow<ProfileResponse>

    val profilePhotoResponse: Flow<ProfileResponse>

    val putProfile: Flow<ProfileResponse>

    fun putProfile(profileRequest: ProfileRequest)

    fun putProfileImage(profileRequestPhoto: ProfileRequestPhoto)

    fun openMainScreen()


    fun getProfile()

    fun openProfileScreen()

    val getContact:Flow<ContactResponse>

    fun openChatScreen()

    fun openLoginScreen()

}