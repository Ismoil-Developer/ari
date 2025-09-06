package uz.mrx.arigo.app

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.transport.TransportFactory
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.HiltAndroidApp
import uz.mrx.arigo.data.local.shp.MySharedPreference
import uz.mrx.arigo.data.remote.websocket.ClientWebSocketClient
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var webSocketClient: ClientWebSocketClient

    @Inject
    lateinit var sharedPreference: MySharedPreference

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()

        // Yandex Map init
        MapKitFactory.setApiKey("79fb340d-f68a-4f8b-b729-8f19f413786d")
        MapKitFactory.initialize(this)
        TransportFactory.initialize(this)

        instance = this

        // Lingver init (til sozlash)
        val languageCode = sharedPreference.language.ifEmpty { "uz" }
        Lingver.init(this, languageCode)

        // ✅ Token bo'lsa ulanish
        val token = sharedPreference.token

        if (token.isNotBlank()) {
            val url = "wss://ari-delivery.uz/ws/goo/connect/"
            webSocketClient.connect(url, token)
        }

    }
}
