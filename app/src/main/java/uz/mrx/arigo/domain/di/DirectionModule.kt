package uz.mrx.arigo.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.mrx.arigo.presentation.direction.confirm.ConfirmScreenDirection
import uz.mrx.arigo.presentation.direction.confirm.impl.ConfirmScreenDirectionImpl
import uz.mrx.arigo.presentation.direction.intro.IntroScreenDirection
import uz.mrx.arigo.presentation.direction.intro.impl.IntroScreenDirectionImpl
import uz.mrx.arigo.presentation.direction.language.LanguageScreenDirection
import uz.mrx.arigo.presentation.direction.language.impl.LanguageScreenDirectionImpl
import uz.mrx.arigo.presentation.direction.location.AddLocationScreenDirection
import uz.mrx.arigo.presentation.direction.location.LocationScreenDirection
import uz.mrx.arigo.presentation.direction.location.SearchMapScreenDirection
import uz.mrx.arigo.presentation.direction.location.impl.AddLocationScreenDirectionImpl
import uz.mrx.arigo.presentation.direction.location.impl.LocationScreenDirectionImpl
import uz.mrx.arigo.presentation.direction.location.impl.SearchMapScreenDirectionImpl
import uz.mrx.arigo.presentation.direction.login.LoginScreenDirection
import uz.mrx.arigo.presentation.direction.login.impl.LoginScreenDirectionImpl
import uz.mrx.arigo.presentation.direction.magazinedetail.MagazineDetailScreenDirection
import uz.mrx.arigo.presentation.direction.magazinedetail.impl.MagazineDetailScreenDirectionImpl
import uz.mrx.arigo.presentation.direction.main.MainScreenDirection
import uz.mrx.arigo.presentation.direction.main.impl.MainScreenDirectionImpl
import uz.mrx.arigo.presentation.direction.profile.ProfileScreenDirection
import uz.mrx.arigo.presentation.direction.profile.impl.ProfileScreenDirectionImpl
import uz.mrx.arigo.presentation.direction.register.RegisterScreenDirection
import uz.mrx.arigo.presentation.direction.register.impl.RegisterScreenDirectionImpl
import uz.mrx.arigo.presentation.direction.searchdelivery.SearchDeliveryScreenDirection
import uz.mrx.arigo.presentation.direction.searchdelivery.impl.SearchDeliveryScreenDirectionImpl
import uz.mrx.arigo.presentation.direction.shop.ShopListScreenDirection
import uz.mrx.arigo.presentation.direction.shop.impl.ShopListScreenDirectionImpl
import uz.mrx.arigo.presentation.direction.splash.SplashScreenDirection
import uz.mrx.arigo.presentation.direction.splash.impl.SplashScreenDirectionImpl

@Module
@InstallIn(ViewModelComponent::class)
interface DirectionModule {

    @[Binds]
    fun bindSplashScreenDirection(impl: SplashScreenDirectionImpl): SplashScreenDirection

    @[Binds]
    fun bindLanguageScreenDirection(impl: LanguageScreenDirectionImpl): LanguageScreenDirection

    @[Binds]
    fun bindIntroScreenDirection(impl: IntroScreenDirectionImpl): IntroScreenDirection

    @[Binds]
    fun bindConfirmDirection(impl: ConfirmScreenDirectionImpl): ConfirmScreenDirection

    @[Binds]
    fun bindLoginDirection(impl: LoginScreenDirectionImpl): LoginScreenDirection

    @[Binds]
    fun bindRegisterDirection(impl: RegisterScreenDirectionImpl): RegisterScreenDirection

    @[Binds]
    fun bindMainScreenDirection(impl: MainScreenDirectionImpl): MainScreenDirection

    @[Binds]
    fun bindMagazineDetailScreenDirection(impl: MagazineDetailScreenDirectionImpl): MagazineDetailScreenDirection

    @[Binds]
    fun bindLocationScreenDirection(impl: LocationScreenDirectionImpl): LocationScreenDirection

    @[Binds]
    fun bindAddLocationScreenDirection(impl: AddLocationScreenDirectionImpl): AddLocationScreenDirection

    @[Binds]
    fun bindSearchMapScreenDirection(impl: SearchMapScreenDirectionImpl): SearchMapScreenDirection

    @[Binds]
    fun bindShopListScreenDirection(impl: ShopListScreenDirectionImpl): ShopListScreenDirection

    @[Binds]
    fun bindProfileScreenDirection(impl: ProfileScreenDirectionImpl): ProfileScreenDirection

    @[Binds]
    fun bindSearchDeliveryScreenDirection(impl: SearchDeliveryScreenDirectionImpl): SearchDeliveryScreenDirection

}