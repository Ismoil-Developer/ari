package uz.mrx.arigo.presentation.ui.viewmodel.homepage

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.response.feature.RoleResponse
import uz.mrx.arigo.data.remote.response.location.ActiveAddressResponse

interface HomePageViewModel {

    fun openMagazineDetailScreen(id:Int)

    fun openLocationScreen()

    fun openShopListScreen(id: Int)

    val featureResponse:Flow<List<RoleResponse>>

    fun openNotification()

    val getActiveAddress:Flow<ActiveAddressResponse>

}