package uz.mrx.arigo.data.remote.response.location

data class LocationDetailResponse(
    val id: Int,
    val custom_name: String,
    val address: String,
    val coordinates: Coordinates,
    val active: Boolean
)


