package uz.mrx.arigo.domain.usecase.location.impl

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.request.location.LocationCreateRequest
import uz.mrx.arigo.data.remote.response.location.ActiveAddressResponse
import uz.mrx.arigo.data.remote.response.location.LocationActiveResponse
import uz.mrx.arigo.data.remote.response.location.LocationCreateResponse
import uz.mrx.arigo.data.remote.response.location.LocationDetailResponse
import uz.mrx.arigo.data.repository.location.LocationRepository
import uz.mrx.arigo.domain.usecase.location.LocationUseCase
import uz.mrx.arigo.utils.ResultData
import javax.inject.Inject

class LocationUseCaseImpl @Inject constructor(private val repository: LocationRepository) :LocationUseCase {

    override suspend fun createLocation(locationCreateRequest: LocationCreateRequest): Flow<ResultData<LocationCreateResponse>> = repository.createLocation(locationCreateRequest)

    override suspend fun getLocation(): Flow<ResultData<List<LocationCreateResponse>>> = repository.getLocation()

    override suspend fun postActiveLocation(id: Int): Flow<ResultData<LocationActiveResponse>> = repository.postActiveLocation(id)

    override suspend fun getLocationDetail(id: Int): Flow<ResultData<LocationDetailResponse>> = repository.getLocationDetail(id)

    override suspend fun getActiveAddress(): Flow<ResultData<ActiveAddressResponse>> = repository.getActiveAddress()

}