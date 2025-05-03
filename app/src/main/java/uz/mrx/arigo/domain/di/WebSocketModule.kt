package uz.mrx.arigo.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.mrx.arigo.data.remote.websocket.ClientWebSocketClient

@Module
@InstallIn(SingletonComponent::class)
object WebSocketModule {

    @Provides
    fun provideCourierWebSocketClient(): ClientWebSocketClient {
        return ClientWebSocketClient()
    }


}
