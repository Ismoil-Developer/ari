package uz.mrx.arigo.presentation.ui.viewmodel.shop

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.response.feature.shoplist.ShopListResponse

interface ShopListScreenViewModel {

    fun openMapSearchScreen(id: Int, roleId: Int)

    fun openShopList(id:Int)

    val responseShopList:Flow<List<ShopListResponse>>

    fun openShopDetailScreen(id: Int, roleId:Int)

    fun openShopSearchList(id: Int, query: String)


}