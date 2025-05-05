package uz.mrx.arigo.data.remote.websocket

sealed class WebSocketGooEvent {

    data class CourierNotFound(
        val order_id: Int,
        val details: String
    ) : WebSocketGooEvent()

    data class DeliveryAccepted(
        val order_id: Int,
        val deliver_id: String,
        val deliver_name: String?,
        val deliver_phone: String,
        val latest_coords: List<Double>
    ) : WebSocketGooEvent()

    data class UnknownMessage(
        val raw_message: String
    ) : WebSocketGooEvent()
}