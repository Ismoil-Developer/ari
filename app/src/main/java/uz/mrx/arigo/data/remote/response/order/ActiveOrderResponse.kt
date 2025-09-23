package uz.mrx.arigo.data.remote.response.order

data class ActiveOrderResponse(
    val id: Int,
    val item_price: String?, // null bo'lishi mumkin
    val delivery_price: String,
    val total_price: String?, // null bo'lishi mumkin
    val assigned_at: String,
    val direction: String,
    val additional_shop_active: Boolean,
    val direction_additional: String,
    val delivery_duration_min: Double,
    val deliver_user: DeliverUser,
    val customer_location: CustomerLocation,
    val shop_location: ShopLocation,
    val additional_shop_location: AdditionalShopLocation,
    val courier_location: CourierLocation
)

data class DeliverUser(
    val id: String,
    val full_name: String,
    val phone_number: String,
    val rating: Double,
    val avatar: String,
    val role: String
)

data class CustomerLocation(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val address: String
)

data class ShopLocation(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val title: String
)

data class AdditionalShopLocation(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val title: String
)

data class CourierLocation(
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val updated_at: String
)
