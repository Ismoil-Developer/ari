package uz.mrx.arigo.data.remote.response.feature.shoplist

data class ShopListResponse(
    val id: Int,
    val image: String?,
    val title: String,
    val work_start: String,
    val work_end: String,
    val locations: String,
    val is_active: Boolean,
    val role: String
)
