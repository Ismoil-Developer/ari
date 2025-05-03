package uz.mrx.arigo.data.remote.websocket

import android.util.Log
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import okhttp3.*
import org.json.JSONObject
import uz.mrx.arigo.utils.ResultData
import uz.mrx.arigo.utils.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientWebSocketClient @Inject constructor() {

    private val client = OkHttpClient.Builder()
        .retryOnConnectionFailure(true)
        .build()

    private var webSocket: WebSocket? = null

    private val _deliveryAccepted = flow<WebSocketGooEvent.DeliveryAccepted>()
    val deliveryAccepted = _deliveryAccepted.asSharedFlow()

    private val _courierNotFound = flow<WebSocketGooEvent.CourierNotFound>()
    val courierNotFound = _courierNotFound.asSharedFlow()

    private var currentUrl: String? = null
    private var currentToken: String? = null

    fun connect(url: String, token: String) {
        if (webSocket != null) return

        currentUrl = url
        currentToken = token

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("GooWebSocket", "Connection opened")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                parseMessage(text).onSuccess { event ->
                    when (event) {
                        is WebSocketGooEvent.CourierNotFound -> {
                            Log.d("GooWebSocket", "Parsed CourierNotFound: orderId=${event.orderId}, details=${event.details}")
                            _courierNotFound.tryEmit(event)
                            Log.d("GooWebSocket", "Message courierNotFound: $_courierNotFound")

                        }
                        is WebSocketGooEvent.DeliveryAccepted -> {
                            Log.d("GooWebSocket", "Parsed CourierNotFound: orderId=${event.orderId}, details=${event.latestCoords}")

                            _deliveryAccepted.tryEmit(event)
                            Log.d("GooWebSocket", "Message delevery2: ${_deliveryAccepted.map { it
                                .orderId}}")

                        }
                        is WebSocketGooEvent.UnknownMessage -> {
                            Log.d("GooWebSocket", "Unknown message: $event")
                        }

                        else -> {

                        }
                    }
                }.onError { error ->
                    Log.e("GooWebSocket", "Parsing error: ${error.localizedMessage}")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("GooWebSocket", "Error: ${t.localizedMessage}")
                webSocket.cancel()
                this@ClientWebSocketClient.webSocket = null
                reconnect()
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("GooWebSocket", "Connection closed: $reason")
                this@ClientWebSocketClient.webSocket = null
            }
        })
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    fun disconnect() {
        webSocket?.close(1000, "Client closed")
        webSocket = null
    }

    private fun reconnect() {
        currentUrl?.let { url ->
            currentToken?.let { token ->
                connect(url, token)
            }
        }
    }

    private fun parseMessage(text: String): ResultData<WebSocketGooEvent> {
        return try {
            val json = JSONObject(text)
            val type = json.getString("type")

            when (type) {
                "kuryer_topilmadi" -> {
                    val orderId = json.getInt("order_id")
                    val details = json.getString("details")
                    ResultData.success(WebSocketGooEvent.CourierNotFound(orderId, details))
                }

                "zakaz_qabul_qilindi" -> {
                    val orderId = json.getInt("order_id")
                    val deliverId = json.getString("deliver_id")
                    val deliverName = json.optString("deliver_name", null)
                    val deliverPhone = json.getString("deliver_phone")
                    val coords = json.getJSONArray("latest_coords")
                    val latestCoords = listOf(coords.getDouble(0), coords.getDouble(1))
                    ResultData.success(
                        WebSocketGooEvent.DeliveryAccepted(
                            orderId,
                            deliverId,
                            deliverName,
                            deliverPhone,
                            latestCoords
                        )
                    )
                }

                else -> {
                    ResultData.success(WebSocketGooEvent.UnknownMessage(text))
                }
            }
        } catch (e: Exception) {
            ResultData.error(e)
        }
    }
}
