package uz.mrx.arigo.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.mrx.arigo.presentation.direction.chat.ChatScreenDirection
import uz.mrx.arigo.presentation.direction.chat.impl.ChatScreenDirectionImpl
import uz.mrx.arigo.presentation.direction.confirm.ConfirmScreenDirection
import uz.mrx.arigo.presentation.direction.confirm.impl.ConfirmScreenDirectionImpl
import uz.mrx.arigo.presentation.direction.delivery.FindDeliveryScreenDirection
import uz.mrx.arigo.presentation.direction.delivery.impl.FindDeliveryScreenDirectionImpl
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
import uz.mrx.arigo.presentation.direction.order.OrderCompletedScreenDirection
import uz.mrx.arigo.presentation.direction.order.OrderDeliveryScreenDirection
import uz.mrx.arigo.presentation.direction.order.OrderDetailScreenDirection
import uz.mrx.arigo.presentation.direction.order.UpdateOrderScreenDirection
import uz.mrx.arigo.presentation.direction.order.impl.OrderCompletedScreenDirectionImpl
import uz.mrx.arigo.presentation.direction.order.impl.OrderDeliveryScreenDirectionImpl
import uz.mrx.arigo.presentation.direction.order.impl.OrderDetailScreenDirectionImpl
import uz.mrx.arigo.presentation.direction.order.impl.UpdateOrderScreenDirectionImpl
import uz.mrx.arigo.presentation.direction.profile.ProfileScreenDirection
import uz.mrx.arigo.presentation.direction.profile.impl.ProfileScreenDirectionImpl
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

    @[Binds]
    fun bindUpdateOrderScreenDirection(impl: UpdateOrderScreenDirectionImpl): UpdateOrderScreenDirection

    @[Binds]
    fun bindFindDeliveryScreenDirection(impl: FindDeliveryScreenDirectionImpl): FindDeliveryScreenDirection

    @[Binds]
    fun bindOrderDeliveryScreenDirection(impl: OrderDeliveryScreenDirectionImpl): OrderDeliveryScreenDirection

    @[Binds]
    fun bindChatScreenDirection(impl: ChatScreenDirectionImpl): ChatScreenDirection

    @[Binds]
    fun bindOrderDetailScreenDirection(impl: OrderDetailScreenDirectionImpl): OrderDetailScreenDirection

    @[Binds]
    fun bindOrderCompletedScreenDirection(impl: OrderCompletedScreenDirectionImpl): OrderCompletedScreenDirection

}