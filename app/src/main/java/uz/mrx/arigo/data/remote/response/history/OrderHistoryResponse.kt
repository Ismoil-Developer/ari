package uz.mrx.arigo.data.remote.response.history

data class OrderHistoryResponse(
    val id: Int,
    val shop_title: String,
    val item_price: String?, // null bo'lishi mumkin
    val created_at: String
)
