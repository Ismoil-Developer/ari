package uz.mrx.arigo.data.remote.websocket

import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import okhttp3.*
import org.json.JSONObject
import uz.mrx.arigo.utils.ResultData
import javax.inject.Inject
import javax.inject.Singleton

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

    private val _orderDirectionUpdate = MutableSharedFlow<WebSocketGooEvent.OrderDirectionUpdate>(replay = 1)
    val orderDirectionUpdate: SharedFlow<WebSocketGooEvent.OrderDirectionUpdate> = _orderDirectionUpdate

    private val _searching = MutableSharedFlow<WebSocketGooEvent.Searching>(replay = 1)
    val searching: SharedFlow<WebSocketGooEvent.Searching> = _searching

    private val _locationUpdate = MutableSharedFlow<WebSocketGooEvent.LocationUpdate>(replay = 1)
    val locationUpdate: SharedFlow<WebSocketGooEvent.LocationUpdate> = _locationUpdate

    private val _orderPrice = MutableSharedFlow<WebSocketGooEvent.OrderPrice>(replay = 1)
    val orderPrice: SharedFlow<WebSocketGooEvent.OrderPrice> = _orderPrice


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
                            Log.d("GooWebSocket", "CourierNotFound: ${event.id}")
                            _courierNotFound.tryEmit(event)
                        }

                        is WebSocketGooEvent.DeliveryAccepted -> {
                            Log.d("GooWebSocket", "DeliveryAccepted: ${event.order_id}")
                            _deliveryAccepted.tryEmit(event)
                        }

                        is WebSocketGooEvent.OrderDirectionUpdate -> {
                            Log.d("GooWebSocket", "OrderDirectionUpdate: ${event.direction}")
                            _orderDirectionUpdate.tryEmit(event)
                        }

                        is WebSocketGooEvent.Searching -> {
                            Log.d("GooWebSocket", "Searching: ${event.shop_title}")
                            _searching.tryEmit(event)
                        }

                        is WebSocketGooEvent.LocationUpdate -> {
                            Log.d("GooWebSocket", "LocationUpdate: ${event.latitude}, ${event.longitude}")
                            _locationUpdate.tryEmit(event)
                        }

                        is WebSocketGooEvent.OrderPrice -> {
                            Log.d("GooWebSocket", "OrderPrice: ${event.total_price}")
                            _orderPrice.tryEmit(event)
                        }

                        else -> {
                            Log.d("GooWebSocket", "Unknown message: $event")
                        }

                    }
                }.onError {
                    Log.e("GooWebSocket", "Parsing error: ${it.localizedMessage}")
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("GooWebSocket", "WebSocket failure: ${t.localizedMessage}")
                webSocket.cancel()
                this@ClientWebSocketClient.webSocket = null
                reconnect()
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("GooWebSocket", "WebSocket closed: $reason")
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
                    val event = WebSocketGooEvent.CourierNotFound(
                        id = json.getString("id"),
                        shop_title = json.getString("shop_title"),
                        shop_id = json.getString("shop_id"),
                        items = json.getString("items"),
                        created_at = json.getString("created_at"),
                        status = json.getString("status")
                    )
                    ResultData.success(event)
                }

                "zakaz_qabul_qilindi" -> {
                    val coords = json.getJSONArray("latest_coords")
                    val event = WebSocketGooEvent.DeliveryAccepted(
                        order_id = json.getInt("order_id"),
                        deliver_id = json.getString("deliver_id"),
                        deliver_name = json.optString("deliver_name", null),
                        deliver_phone = json.getString("deliver_phone"),
                        latest_coords = listOf(coords.getDouble(0), coords.getDouble(1))
                    )
                    ResultData.success(event)
                }

                "order_direction_update" -> {
                    val event = WebSocketGooEvent.OrderDirectionUpdate(
                        order_id = json.getInt("order_id"),
                        direction = json.getString("direction")
                    )
                    ResultData.success(event)
                }

                "searching" -> {
                    val event = WebSocketGooEvent.Searching(
                        id = json.getString("id"),
                        shop_title = json.getString("shop_title"),
                        shop_id = json.getString("shop_id"),
                        items = json.getString("items"),
                        created_at = json.getString("created_at"),
                        status = json.getString("status")
                    )
                    ResultData.success(event)
                }

                "location_update" -> {
                    val event = WebSocketGooEvent.LocationUpdate(
                        user_id = json.getString("user_id"),
                        latitude = json.getDouble("latitude"),
                        longitude = json.getDouble("longitude"),
                        timestamp = json.getString("timestamp")
                    )
                    ResultData.success(event)
                }

                "order_price" -> {
                    val event = WebSocketGooEvent.OrderPrice(
                        order_id = json.getInt("order_id"),
                        delivery_price = json.getString("delivery_price"),
                        item_price = json.getString("item_price"),
                        total_price = json.getString("total_price")
                    )
                    ResultData.success(event)
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
