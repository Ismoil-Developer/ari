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

        // WebSocket connect qilish
        val token = sharedPreference.token
        val url = "ws://ari.digitallaboratory.uz/ws/goo/connect/" // Asl URL'ni yozing

        if (token.isNotEmpty()) {
            webSocketClient.connect(url, token)
        }

        val languageCode = sharedPreference.language // "uz", "en", "ru", etc.
        Lingver.init(this, languageCode)

    }

}
