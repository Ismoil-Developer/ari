package uz.mrx.arigo.domain.usecase.profile

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.response.profile.ContactResponse
import uz.mrx.arigo.data.remote.response.profile.ProfileResponse
import uz.mrx.arigo.utils.ResultData

interface ProfileUseCase {


    suspend fun getProfile(): Flow<ResultData<ProfileResponse>>

    suspend fun putProfile(
        fullName: String,
        phoneNumber:String,
    ): Flow<ResultData<ProfileResponse>>

    suspend fun putProfilePhoto(
        photo: Uri
    ): Flow<ResultData<ProfileResponse>>

    suspend fun getContact():Flow<ResultData<ContactResponse>>


}