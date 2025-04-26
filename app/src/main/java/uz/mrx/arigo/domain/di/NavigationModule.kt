package uz.mrx.arigo.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.mrx.arigo.presentation.navigation.NavigationDispatcher
import uz.mrx.arigo.presentation.navigation.NavigationHandler
import uz.mrx.arigo.presentation.navigation.Navigator

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @Binds
    fun navigator(dispatcher: NavigationDispatcher): Navigator

    @Binds
    fun navigatorHandler(dispatcher: NavigationDispatcher): NavigationHandler


}