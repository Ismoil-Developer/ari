package uz.mrx.arigo.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.mrx.arigo.data.repository.feature.FeatureRepository
import uz.mrx.arigo.data.repository.feature.impl.FeatureRepositoryImpl
import uz.mrx.arigo.data.repository.location.LocationRepository
import uz.mrx.arigo.data.repository.location.impl.LocationRepositoryImpl
import uz.mrx.arigo.data.repository.order.OrderRepository
import uz.mrx.arigo.data.repository.order.impl.OrderRepositoryImpl
import uz.mrx.arigo.data.repository.profile.ProfileRepository
import uz.mrx.arigo.data.repository.profile.impl.ProfileRepositoryImpl
import uz.mrx.arigo.data.repository.register.RegisterRepository
import uz.mrx.arigo.data.repository.register.impl.RegisterRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @[Binds Singleton]
    fun bindRegisterRepository(impl: RegisterRepositoryImpl): RegisterRepository

    @[Binds Singleton]
    fun bindFeatureRepository(impl: FeatureRepositoryImpl): FeatureRepository

    @[Binds Singleton]
    fun bindLocationRepository(impl: LocationRepositoryImpl): LocationRepository

    @[Binds Singleton]
    fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @[Binds Singleton]
    fun bindOrderRepository(impl: OrderRepositoryImpl): OrderRepository



}