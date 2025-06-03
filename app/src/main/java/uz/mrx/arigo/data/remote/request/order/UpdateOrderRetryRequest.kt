package uz.mrx.arigo.data.remote.request.order

data class UpdateOrderRetryRequest(
    val items: String,
    val house_number: String,
    val apartment_number: String,
    val floor: Int, // nullable
    val intercom_code: String,
    val additional_note: String
)
