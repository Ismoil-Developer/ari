package uz.mrx.arigo.data.remote.response.history

data class OrderHistoryDetailResponse(
    val item_price: String?, // null bo'lishi mumkin
    val delivery_price: String,
    val total_price: String?, // null bo'lishi mumkin
    val created_at: String,
    val shop_id: Int,
    val shop_title: String,
    val shop_image: String,
    val user_location_address: String
)
