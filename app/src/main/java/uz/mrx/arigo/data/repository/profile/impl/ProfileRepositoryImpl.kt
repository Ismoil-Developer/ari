package uz.mrx.arigo.data.repository.profile.impl

import android.content.Context
import android.net.Uri
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import uz.mrx.arigo.data.remote.api.ProfileApi
import uz.mrx.arigo.data.remote.response.profile.ProfileResponse
import uz.mrx.arigo.data.repository.profile.ProfileRepository
import uz.mrx.arigo.utils.ResultData
import java.io.File
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(private val api: ProfileApi, @ApplicationContext private val context: Context) : ProfileRepository {


    override suspend fun getProfile() = channelFlow<ResultData<ProfileResponse>> {

        try {

            val response = api.getProfile()

            if (response.isSuccessful) {

                val profileResponse = response.body()
                if (profileResponse != null) {
                    trySend(ResultData.success(profileResponse))
                } else {
                    close(Exception("No data found for the provided ID"))
                }
            } else {
                val errorMessage = "Error ${response.code()}: ${response.message()}"
                Log.e("API_ERROR", errorMessage)
                close(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("API_EXCEPTION", "Exception while fetching data by ID: ${e.message}")
            close(e)
        }
    }



    override suspend fun putProfilePhoto(photo: Uri) = channelFlow<ResultData<ProfileResponse>> {

        try {
            val photoPart = photo?.let {
                prepareFilePart(it)
            }

            val response = api.updateProfilePhoto(photoPart)

            if (response.isSuccessful){
                val baseResponse = response.body()
                if (baseResponse != null) {

                    trySend(ResultData.success(baseResponse))
                } else {
                    trySend(ResultData.messageText("Unknown error"))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Not load"
                trySend(ResultData.error(Throwable(errorMessage)))
            }
        } catch (e: Exception) {
            trySend(ResultData.error(e))
        }
    }.catch { emit(ResultData.error(it)) }

    override suspend fun putProfile(
        fullName: String,
        phoneNumber:String,
    ) = channelFlow<ResultData<ProfileResponse>> {
        try {
            // RequestBody yaratish
            val fullNameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), fullName)
            val phoneNumberBody = RequestBody.create("text/plain".toMediaTypeOrNull(), phoneNumber)

            // Rasmni MultipartBody.Part ga aylantirish

            val response = api.updateUserProfile(fullNameBody, phoneNumberBody)

            if (response.isSuccessful) {
                val baseResponse = response.body()
                if (baseResponse != null) {

                    trySend(ResultData.success(baseResponse))
                } else {
                    trySend(ResultData.messageText("Unknown error"))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Not load"
                trySend(ResultData.error(Throwable(errorMessage)))
            }
        } catch (e: Exception) {
            trySend(ResultData.error(e))
        }
    }.catch { emit(ResultData.error(it)) }


    private fun prepareFilePart(uri: Uri): MultipartBody.Part {
        val file = File(context.cacheDir, "profile_photo.jpg")
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            file.outputStream().use { outputStream -> inputStream.copyTo(outputStream) }
        }
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData("avatar", file.name, requestFile)
    }
}