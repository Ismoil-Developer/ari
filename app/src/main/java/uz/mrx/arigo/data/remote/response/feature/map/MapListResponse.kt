package uz.mrx.arigo.data.remote.response.feature.map

data class MapListResponse(
    val id: Int,
    val image: String?,
    val coordinates: Coordinates,
    val locations: String,
    val title: String,
    val is_active: Boolean,
    val role: String
)

data class Coordinates(
    val type: String,
    val coordinates: List<Double>
)