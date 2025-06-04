package uz.mrx.arigo.data.remote.websocket

import android.util.Log
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import okhttp3.*
import org.json.JSONObject
import uz.mrx.arigo.utils.ResultData
import uz.mrx.arigo.utils.flow
import javax.inject.Inject
import javax.inject.Singleton
// Top part importlarni to'g'ri saqlab qoling
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Singleton
class ClientWebSocketClient @Inject constructor() {

    private val client = OkHttpClient.Builder()
        .retryOnConnectionFailure(true)
        .build()

    private var webSocket: WebSocket? = null

    private val _deliveryAccepted = MutableSharedFlow<WebSocketGooEvent.DeliveryAccepted>(replay = 1)
    val deliveryAccepted: SharedFlow<WebSocketGooEvent.DeliveryAccepted> = _deliveryAccepted

    private val _courierNotFound = MutableSharedFlow<WebSocketGooEvent.CourierNotFound>(replay = 1)
    val courierNotFound: SharedFlow<WebSocketGooEvent.CourierNotFound> = _courierNotFound

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
                            Log.d("GooWebSocket", "Parsed CourierNotFound: ${event.id}")
                            _courierNotFound.tryEmit(event)
                        }

                        is WebSocketGooEvent.DeliveryAccepted -> {
                            Log.d("GooWebSocket", "Parsed Delivery Accepted: ${event.order_id}")
                            _deliveryAccepted.tryEmit(event)
                        }

                        is WebSocketGooEvent.Searching -> {
                            Log.d("GooWebSocket", "Parsed Searching: ${event.shop_title}")
                            // Optional: Add a new shared flow and emit it if needed
                        }

                        else -> Log.d("GooWebSocket", "Unknown message: $event")
                    }
                }.onError {
                    Log.e("GooWebSocket", "Parsing error: ${it.localizedMessage}")
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
        if (currentUrl != null && currentToken != null) {
            connect(currentUrl!!, currentToken!!)
        }
    }

    private fun parseMessage(text: String): ResultData<WebSocketGooEvent> {
        return try {
            val json = JSONObject(text)
            val type = json.getString("type")
            when (type) {
                "kuryer_topilmadi" -> {
                    val id = json.getString("id")
                    val shopTitle = json.getString("shop_title")
                    val shopId = json.getString("shop_id")
                    val items = json.getString("items")
                    val createdAt = json.getString("created_at")
                    val status = json.getString("status")
                    ResultData.success(
                        WebSocketGooEvent.CourierNotFound(
                            id, shopTitle, shopId, items, createdAt, status
                        )
                    )
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

                "searching" -> {
                    val id = json.getString("id")
                    val shopTitle = json.getString("shop_title")
                    val shopId = json.getString("shop_id")
                    val items = json.getString("items")
                    val createdAt = json.getString("created_at")
                    val status = json.getString("status")

                    ResultData.success(
                        WebSocketGooEvent.Searching(
                            id, shopTitle, shopId, items, createdAt, status
                        )
                    )
                }

                else -> ResultData.success(WebSocketGooEvent.UnknownMessage(text))
            }
        } catch (e: Exception) {
            ResultData.error(e)
        }
    }

}
