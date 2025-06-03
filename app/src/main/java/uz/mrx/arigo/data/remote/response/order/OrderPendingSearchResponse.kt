package uz.mrx.arigo.data.remote.response.order

data class OrderPendingSearchResponse(
    val id: Int,
    val order_code: String,
    val shop_title: String,
    val shop_id: String,
    val items: String,
    val created_at: String,
    val status: String
)
