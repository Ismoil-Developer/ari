package uz.mrx.arigo.data.remote.request.order

import com.google.gson.annotations.SerializedName

data class UpdateOrderRequest(
    @SerializedName("house_number")
    val houseNumber: String,

    @SerializedName("apartment_number")
    val apartmentNumber: String,

    val floor: Int,

    @SerializedName("intercom_code")
    val intercomCode: String,

    @SerializedName("additional_note")
    val additionalNote: String

)
