package uz.mrx.arigo.presentation.ui.viewmodel.shop

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.response.feature.map.MapListResponse

interface SearchMapScreenViewModel {

    fun getMapList(id:Int, radius:Int)

    val searchMapListResponse: Flow<List<MapListResponse>>

    fun openShopDetail(id:Int, roleId:Int)

}