package uz.mrx.arigo.data.remote.response.order

data class OrderDetailResponse(
    val items: String,
    val allow_other_shops: Boolean,
    val house_number: String?,
    val apartment_number: String?,
    val floor: String?,
    val intercom_code: String?,
    val additional_note: String?,
    val shop: Shop,
    val user: User
)

data class Shop(
    val title: String,
    val image: String
)

data class User(
    val phone_number: String,
    val active_location: ActiveLocation
)

data class ActiveLocation(
    val id:Int,
    val address: String,
    val coordinates: Coordinates
)

data class Coordinates(
    val type: String,
    val coordinates: List<Double>
)
