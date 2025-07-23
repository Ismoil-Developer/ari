package uz.mrx.arigo.domain.usecase.feature

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.response.feature.RoleResponse
import uz.mrx.arigo.data.remote.response.feature.advertising.AdvertisingResponse
import uz.mrx.arigo.data.remote.response.feature.all.ShopAllResponse
import uz.mrx.arigo.data.remote.response.feature.detail.FeatureDetailResponse
import uz.mrx.arigo.data.remote.response.feature.feedback.FeedBackRequest
import uz.mrx.arigo.data.remote.response.feature.feedback.FeedBackResponse
import uz.mrx.arigo.data.remote.response.feature.map.MapListResponse
import uz.mrx.arigo.data.remote.response.feature.role.ShopRoleResponse
import uz.mrx.arigo.data.remote.response.feature.shoplist.ShopListResponse
import uz.mrx.arigo.utils.ResultData

interface FeatureUseCase {

    suspend fun getFeatures(): Flow<ResultData<List<RoleResponse>>>

    suspend fun getRole():Flow<ResultData<List<ShopRoleResponse>>>

    suspend fun getAdvertising():Flow<ResultData<List<AdvertisingResponse>>>

    suspend fun getFeaturesDetail(id:Int):Flow<ResultData<FeatureDetailResponse>>

    suspend fun getShopsAll(id:Int):Flow<ResultData<List<ShopAllResponse>>>

    suspend fun getShopList(id: Int):Flow<ResultData<List<ShopListResponse>>>

    suspend fun getMapList(id: Int, radius:Int):Flow<ResultData<List<MapListResponse>>>

    suspend fun getShopSearchList(id: Int, query: String): Flow<ResultData<List<ShopListResponse>>>

    suspend fun postFeedBack(id: Int, feedBackRequest: FeedBackRequest):Flow<ResultData<FeedBackResponse>>

}