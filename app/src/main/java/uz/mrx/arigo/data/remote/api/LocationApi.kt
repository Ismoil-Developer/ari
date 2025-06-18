package uz.mrx.arigo.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import uz.mrx.arigo.data.remote.request.location.LocationCreateRequest
import uz.mrx.arigo.data.remote.response.feature.map.MapListResponse
import uz.mrx.arigo.data.remote.response.feature.shoplist.ShopListResponse
import uz.mrx.arigo.data.remote.response.location.ActiveAddressResponse
import uz.mrx.arigo.data.remote.response.location.LocationActiveResponse
import uz.mrx.arigo.data.remote.response.location.LocationCreateResponse
import uz.mrx.arigo.data.remote.response.location.LocationDeleteResponse
import uz.mrx.arigo.data.remote.response.location.LocationDetailResponse

interface LocationApi {

    @POST("/goo/locations/create/")
    suspend fun createLocation(@Body locationCreateRequest: LocationCreateRequest):Response<LocationCreateResponse>

    @GET("/goo/locations/")
    suspend fun getLocations():Response<List<LocationCreateResponse>>

    @POST("/goo/locations/{id}/active/")
    suspend fun postActiveLocation(@Path("id") id:Int, @Body locationCreateRequest: LocationCreateRequest):Response<LocationActiveResponse>

    @GET("/goo/locations/{id}/detail/")
    suspend fun getLocationDetail(@Path("id") id: Int):Response<LocationDetailResponse>

    @GET("/goo/locations-active/")
    suspend fun getActiveAddress():Response<ActiveAddressResponse>

//    @GET("/shop/shop-map-list/{id}/")
//    suspend fun getMapListFilter(@Path("id") id: Int):Response<List<MapListResponse>>

    @DELETE("/goo/locations/{id}/delete/")
    suspend fun deleteLocation(@Path("id") id: Int): Response<LocationDeleteResponse>

}