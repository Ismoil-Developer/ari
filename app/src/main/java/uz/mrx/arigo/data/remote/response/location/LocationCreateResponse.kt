package uz.mrx.arigo.data.remote.response.location

data class LocationCreateResponse(
    val id: Int,
    val custom_name: String,
    val address: String,
    val coordinates: Coordinates,
    val created_at: String,
    val active: Boolean
)

data class Coordinates(
    val type: String,
    val coordinates: List<Double>
)
