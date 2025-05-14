package uz.mrx.arigo.data.repository.feature.impl

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import uz.mrx.arigo.data.remote.api.FeatureApi
import uz.mrx.arigo.data.remote.response.feature.RoleResponse
import uz.mrx.arigo.data.remote.response.feature.all.ShopAllResponse
import uz.mrx.arigo.data.remote.response.feature.detail.FeatureDetailResponse
import uz.mrx.arigo.data.remote.response.feature.map.MapListResponse
import uz.mrx.arigo.data.remote.response.feature.role.ShopRoleResponse
import uz.mrx.arigo.data.remote.response.feature.shoplist.ShopListResponse
import uz.mrx.arigo.data.repository.feature.FeatureRepository
import uz.mrx.arigo.utils.ResultData
import javax.inject.Inject

class FeatureRepositoryImpl @Inject constructor(private val api: FeatureApi):FeatureRepository {

    override suspend fun getFeatures() = channelFlow<ResultData<List<RoleResponse>>> {
        try {

            val response = api.getFeatures()

            if (response.isSuccessful) {

                if (response != null) {
                    val responseObject = response.body() as List<RoleResponse>

                    trySend(ResultData.success(responseObject))
                } else {
                    trySend(ResultData.error(Throwable(response.message())))
                }
            } else {
                trySend(ResultData.messageText(response.message()))
            }
        } catch (e: Exception) {
            trySend(ResultData.messageText(e.message.toString()))
        }
    }.catch { emit(ResultData.error(it)) }

    override suspend fun getRole() = channelFlow<ResultData<List<ShopRoleResponse>>> {
        try {

            val response = api.getRole()

            if (response.isSuccessful) {

                if (response != null) {
                    val responseObject = response.body() as List<ShopRoleResponse>

                    trySend(ResultData.success(responseObject))
                } else {
                    trySend(ResultData.error(Throwable(response.message())))
                }
            } else {
                trySend(ResultData.messageText(response.message()))
            }
        } catch (e: Exception) {
            trySend(ResultData.messageText(e.message.toString()))
        }
    }.catch { emit(ResultData.error(it)) }


    override suspend fun getFeaturesDetail(id: Int) = channelFlow<ResultData<FeatureDetailResponse>> {
        try {
            val response = api.getFeaturesDetail(id)
            Log.d("RRRRRRRR", "getFeaturesDetail: $id")
            if (response.isSuccessful) {
                val responseDetail = response.body()
                if (responseDetail != null) {
                    Log.d("RRRRRRRR", "getFeaturesDetail: $responseDetail")
                    trySend(ResultData.success(responseDetail))
                } else {
                    trySend(ResultData.messageText("Bo‘sh ma’lumot qaytdi"))
                }
            } else {
                trySend(ResultData.messageText("Xatolik: ${response.message()}"))
            }
        } catch (e: Exception) {
            trySend(ResultData.messageText("Exception: ${e.localizedMessage}"))
        }
    }.catch { emit(ResultData.error(it)) }




    override suspend fun getShopsAll(id: Int) = channelFlow<ResultData<List<ShopAllResponse>>> {
        try {

            val response = api.getShopsAll(id)

            if (response.isSuccessful) {

                if (response != null) {
                    val responseObject = response.body() as List<ShopAllResponse>

                    trySend(ResultData.success(responseObject))
                } else {
                    trySend(ResultData.error(Throwable(response.message())))
                }
            } else {
                trySend(ResultData.messageText(response.message()))
            }
        } catch (e: Exception) {
            trySend(ResultData.messageText(e.message.toString()))
        }
    }.catch { emit(ResultData.error(it)) }


    override suspend fun getMapList(id: Int, radius:Int) = channelFlow<ResultData<List<MapListResponse>>> {
        try {
            val response = api.getMapList(id, radius)
            if (response.isSuccessful) {
                val newsResponse = response.body() as List<MapListResponse>

                trySend(ResultData.success(newsResponse))

            } else {
                trySend(ResultData.messageText(response.message()))
            }
        } catch (e: Exception) {
            trySend(ResultData.messageText(e.message.toString()))
        }
    }.catch { emit(ResultData.error(it)) }


    override suspend fun getShopList(id: Int) = channelFlow<ResultData<List<ShopListResponse>>> {
        try {
            val response = api.getShopList(id)
            if (response.isSuccessful) {
                val newsResponse = response.body() as List<ShopListResponse>

                trySend(ResultData.success(newsResponse))

            } else {
                trySend(ResultData.messageText(response.message()))
            }
        } catch (e: Exception) {
            trySend(ResultData.messageText(e.message.toString()))
        }
    }.catch { emit(ResultData.error(it)) }

    override suspend fun getShopSearchList(id: Int, query: String) = channelFlow<ResultData<List<ShopListResponse>>> {
        try {
            val response = api.getShopSearchList(id, query)  // Pass the query to API call

            if (response.isSuccessful) {
                val responseList = response.body() ?: emptyList()

                trySend(ResultData.success(responseList))  // Send the response back wrapped in ResultData
            } else {
                trySend(ResultData.messageText(response.message()))  // Send error message if not successful
            }
        } catch (e: Exception) {
            trySend(ResultData.messageText(e.message.toString()))  // Catch any exceptions and send error message
        }
    }.catch { emit(ResultData.error(it)) }




}