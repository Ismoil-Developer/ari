package uz.mrx.arigo.data.remote.response.register

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("non_field_errors") val errors: List<String>? = null
)
