package uz.mrx.arigo.data.repository.location

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.request.location.LocationCreateRequest
import uz.mrx.arigo.data.remote.response.location.ActiveAddressResponse
import uz.mrx.arigo.data.remote.response.location.LocationActiveResponse
import uz.mrx.arigo.data.remote.response.location.LocationCreateResponse
import uz.mrx.arigo.data.remote.response.location.LocationDetailResponse
import uz.mrx.arigo.utils.ResultData

interface LocationRepository {

    suspend fun createLocation(locationCreateRequest: LocationCreateRequest): Flow<ResultData<LocationCreateResponse>>

    suspend fun getLocation():Flow<ResultData<List<LocationCreateResponse>>>

    suspend fun postActiveLocation(id:Int):Flow<ResultData<LocationActiveResponse>>

    suspend fun getLocationDetail(id: Int):Flow<ResultData<LocationDetailResponse>>

    suspend fun getActiveAddress():Flow<ResultData<ActiveAddressResponse>>

}