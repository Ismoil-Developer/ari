package uz.mrx.arigo.data.remote.response.order

import com.google.gson.annotations.SerializedName

data class OrderResponse(
    val id: Int,
    val user: String,
    val shop: Int,
    val additional_shop:Int,
    val deliver: String?,
    val items: String,
    @SerializedName("allow_other_shops")
    val allowOtherShops: Boolean,
    val status: String,
    @SerializedName("created_at")
    val createdAt: String
)
