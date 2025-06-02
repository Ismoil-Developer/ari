package uz.mrx.arigo.data.remote.api

import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part
import uz.mrx.arigo.data.remote.response.profile.ContactResponse
import uz.mrx.arigo.data.remote.response.profile.ProfileResponse

interface ProfileApi {

    @GET("/goo/profile-get/")
    suspend fun getProfile():Response<ProfileResponse>

    @Multipart
    @PATCH("/goo/profile/update/")
    suspend fun updateUserProfile(
        @Part("full_name") fullName: RequestBody,
        @Part("phone_number") phoneNumber: RequestBody,
    ): Response<ProfileResponse>

    @Multipart
    @PATCH("/goo/profile/update/")
    suspend fun updateProfilePhoto(
        @Part avatar: MultipartBody.Part?
    ):Response<ProfileResponse>

    @GET("/goo/contact/")
    suspend fun getContact():Response<ContactResponse>

}