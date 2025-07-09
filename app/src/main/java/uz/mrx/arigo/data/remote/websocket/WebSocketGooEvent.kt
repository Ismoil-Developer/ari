package uz.mrx.arigo.data.remote.websocket

sealed class WebSocketGooEvent {

    data class CourierNotFound(
        val id: String,
        val shop_title: String,
        val shop_id: String,
        val items: String,
        val created_at: String,
        val status: String
    ) : WebSocketGooEvent()

    data class DeliveryAccepted(
        val order_id: Int,
        val deliver_id: String,
        val deliver_name: String?,
        val deliver_phone: String,
        val latest_coords: List<Double>
    ) : WebSocketGooEvent()

    data class Searching(
        val id: String,
        val shop_title: String,
        val shop_id: String,
        val items: String,
        val created_at: String,
        val status: String
    ) : WebSocketGooEvent()

    data class OrderDirectionUpdate(
        val order_id: Int,
        val direction: String
    ) : WebSocketGooEvent()

    data class LocationUpdate(
        val user_id: String,
        val latitude: Double,
        val longitude: Double,
        val timestamp: String
    ) : WebSocketGooEvent()

    data class OrderPrice(
        val order_id: Int,
        val delivery_price: String,
        val item_price: String,
        val total_price: String
    ) : WebSocketGooEvent()

    data class DurationUpdate(
        val order_id: String,
        val duration_min: Double,
        val timestamp: String
    ) : WebSocketGooEvent()

    data class UnknownMessage(
        val raw_message: String
    ) : WebSocketGooEvent()


}
