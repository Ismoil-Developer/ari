package uz.mrx.arigo.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.mrx.arigo.data.remote.websocket.ClientWebSocketClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {

    @Provides
    @Singleton   // 🔥 MUHIM: faqat bitta instance bo‘lishi uchun
    fun provideCourierWebSocketClient(): ClientWebSocketClient {
        return ClientWebSocketClient()
    }

}
