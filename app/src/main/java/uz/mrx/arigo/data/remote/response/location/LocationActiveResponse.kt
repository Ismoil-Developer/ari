package uz.mrx.arigo.data.remote.response.location

data class LocationActiveResponse(
    val message: String,
    val updated_location: UpdatedLocation
)

data class UpdatedLocation(
    val id: Int,
    val custom_name: String,
    val address: String,
    val coordinates: Coordinates,
    val active: Boolean
)

