package uz.mrx.arigo.data.remote.response.feature

data class RoleResponse(
    val role_id: Int,
    val role: String,
    val shops: List<ShopData>
)

data class ShopData(
    val id: Int,
    val image: String,
    val title: String,
    val is_active: Boolean,
    val locations: String,
    val role: String
)