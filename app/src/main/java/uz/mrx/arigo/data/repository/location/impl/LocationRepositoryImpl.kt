package uz.mrx.arigo.data.repository.location.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import uz.mrx.arigo.data.remote.api.LocationApi
import uz.mrx.arigo.data.remote.request.location.LocationCreateRequest
import uz.mrx.arigo.data.remote.response.location.ActiveAddressResponse
import uz.mrx.arigo.data.remote.response.location.LocationActiveResponse
import uz.mrx.arigo.data.remote.response.location.LocationCreateResponse
import uz.mrx.arigo.data.remote.response.location.LocationDeleteResponse
import uz.mrx.arigo.data.remote.response.location.LocationDetailResponse
import uz.mrx.arigo.data.repository.location.LocationRepository
import uz.mrx.arigo.utils.ResultData
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(private val api: LocationApi) :
    LocationRepository {


    override suspend fun createLocation(locationCreateRequest: LocationCreateRequest) =
        channelFlow<ResultData<LocationCreateResponse>> {

            try {
                val response = api.createLocation(locationCreateRequest)
                if (response.isSuccessful) {
                    val mainData = response.body()
                    if (mainData != null) {
                        trySend(ResultData.success(mainData))
                    } else {
                        trySend(ResultData.messageText("Response body is null"))
                    }
                } else {
                    trySend(ResultData.messageText("Error ${response.code()}: ${response.message()}"))
                }
            } catch (e: Exception) {
                trySend(ResultData.error(e))
            }
        }.catch { e ->
            emit(ResultData.error(e))
        }


    override suspend fun getLocation() = channelFlow<ResultData<List<LocationCreateResponse>>> {
        try {
            val response = api.getLocations()
            if (response.isSuccessful) {
                val newsResponse = response.body() as List<LocationCreateResponse>

                trySend(ResultData.success(newsResponse))

            } else {
                trySend(ResultData.messageText(response.message()))
            }
        } catch (e: Exception) {
            trySend(ResultData.messageText(e.message.toString()))
        }
    }.catch { emit(ResultData.error(it)) }


    override suspend fun postActiveLocation(id: Int) =
        channelFlow<ResultData<LocationActiveResponse>> {
            try {
                val response = api.postActiveLocation(id)
                if (response.isSuccessful) {
                    val mainData = response.body()
                    if (mainData != null) {
                        trySend(ResultData.success(mainData))
                    } else {
                        trySend(ResultData.messageText("Response body is null"))
                    }
                } else {
                    trySend(ResultData.messageText("Error ${response.code()}: ${response.message()}"))
                }
            } catch (e: Exception) {
                trySend(ResultData.error(e))
            }
        }.catch { e ->
            emit(ResultData.error(e))
        }


    override suspend fun getLocationDetail(id: Int) =
        channelFlow<ResultData<LocationDetailResponse>> {
            try {
                val response = api.getLocationDetail(id)
                if (response.isSuccessful) {
                    val mainData = response.body()
                    if (mainData != null) {
                        trySend(ResultData.success(mainData))
                    } else {
                        trySend(ResultData.messageText("Response body is null"))
                    }
                } else {
                    trySend(ResultData.messageText("Error ${response.code()}: ${response.message()}"))
                }
            } catch (e: Exception) {
                trySend(ResultData.error(e))
            }
        }.catch { e ->
            emit(ResultData.error(e))
        }

    override suspend fun getActiveAddress() = channelFlow<ResultData<ActiveAddressResponse>> {
        try {
            val response = api.getActiveAddress()
            if (response.isSuccessful) {
                val mainData = response.body()
                if (mainData != null) {
                    trySend(ResultData.success(mainData))
                } else {
                    trySend(ResultData.messageText("Response body is null"))
                }
            } else {
                trySend(ResultData.messageText("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            trySend(ResultData.error(e))
        }
    }.catch { e ->
        emit(ResultData.error(e))
    }


    override suspend fun deleteLocation(id: Int) = channelFlow<ResultData<LocationDeleteResponse>> {
        try {
            val response = api.deleteLocation(id)
            if (response.isSuccessful) {
                val mainData = response.body()
                if (mainData != null) {
                    trySend(ResultData.success(mainData))
                } else {
                    trySend(ResultData.messageText("Response body is null"))
                }
            } else {
                trySend(ResultData.messageText("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            trySend(ResultData.error(e))
        }
    }.catch { emit(ResultData.error(it)) }



}