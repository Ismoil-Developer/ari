package uz.mrx.arigo.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.mrx.arigo.domain.usecase.feature.FeatureUseCase
import uz.mrx.arigo.domain.usecase.feature.impl.FeatureUseCaseImpl
import uz.mrx.arigo.domain.usecase.location.LocationUseCase
import uz.mrx.arigo.domain.usecase.location.impl.LocationUseCaseImpl
import uz.mrx.arigo.domain.usecase.order.OrderUseCase
import uz.mrx.arigo.domain.usecase.order.impl.OrderUseCaseImpl
import uz.mrx.arigo.domain.usecase.profile.ProfileUseCase
import uz.mrx.arigo.domain.usecase.profile.impl.ProfileUseCaseImpl
import uz.mrx.arigo.domain.usecase.register.RegisterUseCase
import uz.mrx.arigo.domain.usecase.register.impl.RegisterUseCaseImpl
import uz.mrx.arigo.domain.usecase.searchmap.SearchMapUseCase
import uz.mrx.arigo.domain.usecase.searchmap.impl.SearchMapUseCaseImpl

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {


    @Binds
    fun bindRegisterUseCase(impl: RegisterUseCaseImpl): RegisterUseCase

    @Binds
    fun bindSearchMapUseCase(impl: SearchMapUseCaseImpl): SearchMapUseCase

    @Binds
    fun bindFeatureUseCase(impl: FeatureUseCaseImpl): FeatureUseCase

    @Binds
    fun bindLocationUseCase(impl: LocationUseCaseImpl): LocationUseCase

    @Binds
    fun bindProfileUseCase(impl: ProfileUseCaseImpl): ProfileUseCase

    @Binds
    fun bindOrderUseCase(impl: OrderUseCaseImpl): OrderUseCase

}