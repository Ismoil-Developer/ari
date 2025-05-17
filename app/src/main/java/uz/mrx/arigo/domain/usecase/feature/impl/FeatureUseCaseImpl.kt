package uz.mrx.arigo.domain.usecase.feature.impl

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.response.feature.RoleResponse
import uz.mrx.arigo.data.remote.response.feature.advertising.AdvertisingResponse
import uz.mrx.arigo.data.remote.response.feature.all.ShopAllResponse
import uz.mrx.arigo.data.remote.response.feature.detail.FeatureDetailResponse
import uz.mrx.arigo.data.remote.response.feature.map.MapListResponse
import uz.mrx.arigo.data.remote.response.feature.role.ShopRoleResponse
import uz.mrx.arigo.data.remote.response.feature.shoplist.ShopListResponse
import uz.mrx.arigo.data.repository.feature.FeatureRepository
import uz.mrx.arigo.domain.usecase.feature.FeatureUseCase
import uz.mrx.arigo.utils.ResultData
import javax.inject.Inject

class FeatureUseCaseImpl @Inject constructor(private val repository: FeatureRepository) :
    FeatureUseCase {

    override suspend fun getFeatures(): Flow<ResultData<List<RoleResponse>>> =
        repository.getFeatures()

    override suspend fun getRole(): Flow<ResultData<List<ShopRoleResponse>>> = repository.getRole()

    override suspend fun getAdvertising(): Flow<ResultData<List<AdvertisingResponse>>> = repository.getAdvertising()

    override suspend fun getFeaturesDetail(id: Int): Flow<ResultData<FeatureDetailResponse>> =
        repository.getFeaturesDetail(id)

    override suspend fun getShopsAll(id: Int): Flow<ResultData<List<ShopAllResponse>>> =
        repository.getShopsAll(id)

    override suspend fun getShopList(id: Int): Flow<ResultData<List<ShopListResponse>>> =
        repository.getShopList(id)

    override suspend fun getMapList(id: Int, radius:Int): Flow<ResultData<List<MapListResponse>>> =
        repository.getMapList(id, radius)

    override suspend fun getShopSearchList(
        id: Int,
        query: String
    ): Flow<ResultData<List<ShopListResponse>>> = repository.getShopSearchList(id, query)


}