package uz.mrx.arigo.domain.usecase.profile.impl

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.response.profile.ContactResponse
import uz.mrx.arigo.data.remote.response.profile.ProfileResponse
import uz.mrx.arigo.data.repository.profile.ProfileRepository
import uz.mrx.arigo.domain.usecase.profile.ProfileUseCase
import uz.mrx.arigo.utils.ResultData
import javax.inject.Inject

class ProfileUseCaseImpl @Inject constructor(private val repository: ProfileRepository):ProfileUseCase {

    override suspend fun getProfile(): Flow<ResultData<ProfileResponse>> = repository.getProfile()

    override suspend fun putProfile(
        fullName: String,
        phoneNumber: String
    ): Flow<ResultData<ProfileResponse>> = repository.putProfile(fullName, phoneNumber)

    override suspend fun putProfilePhoto(photo: Uri): Flow<ResultData<ProfileResponse>> = repository.putProfilePhoto(photo)

    override suspend fun getContact(): Flow<ResultData<ContactResponse>> = repository.getContact()

}