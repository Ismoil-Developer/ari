package uz.mrx.arigo.data.remote.websocket

sealed class WebSocketGooEvent {

    data class CourierNotFound(
        val orderId: Int,
        val details: String
    ) : WebSocketGooEvent()

    data class DeliveryAccepted(
        val orderId: Int,
        val deliverId: String,
        val deliverName: String?,
        val deliverPhone: String,
        val latestCoords: List<Double>
    ) : WebSocketGooEvent()

    data class UnknownMessage(
        val rawMessage: String
    ) : WebSocketGooEvent()
}