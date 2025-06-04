package uz.mrx.arigo.data.remote.response.order

data class ActiveOrderResponse(
    val id: Int,
    val delivery_price: Int,
    val assigned_at: String,
    val direction: String,
    val delivery_duration_min: Double,
    val deliver_user: DeliverUser,
    val customer_location: CustomerLocation,
    val shop_location: ShopLocation,
    val courier_location: CourierLocation
)

data class DeliverUser(
    val id: String,
    val full_name: String?, // null bo'lishi mumkin
    val phone_number: String,
    val rating: Double,
    val avatar: String?, // null bo'lishi mumkin
    val role: String
)

data class CustomerLocation(
    val latitude: Double,
    val longitude: Double,
    val address: String
)

data class ShopLocation(
    val latitude: Double,
    val longitude: Double,
    val title: String
)

data class CourierLocation(
    val latitude: Double,
    val longitude: Double,
    val updated_at: String
)
