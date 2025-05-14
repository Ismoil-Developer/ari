package uz.mrx.arigo.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uz.mrx.arigo.data.remote.response.feature.RoleResponse
import uz.mrx.arigo.data.remote.response.feature.all.ShopAllResponse
import uz.mrx.arigo.data.remote.response.feature.detail.FeatureDetailResponse
import uz.mrx.arigo.data.remote.response.feature.map.MapListResponse
import uz.mrx.arigo.data.remote.response.feature.role.ShopRoleResponse
import uz.mrx.arigo.data.remote.response.feature.shoplist.ShopListResponse

interface FeatureApi {

    @GET("shop/shop-featured-list/")
    suspend fun getFeatures(): Response<List<RoleResponse>>

    @GET("shop/shop-detail/{id}/")
    suspend fun getFeaturesDetail(@Path("id") id: Int): Response<FeatureDetailResponse>

    @GET("/shop/shop-role-list/")
    suspend fun getRole():Response<List<ShopRoleResponse>>

    @GET("/shop/shop-list/{id}/")
    suspend fun getShopsAll(@Path("id") id:Int):Response<List<ShopAllResponse>>

    @GET("/shop/shop-list/{id}/")
    suspend fun getShopList(@Path("id") id: Int):Response<List<ShopListResponse>>

    @GET("/shop/shop-list/{id}/")
    suspend fun getShopSearchList(
        @Path("id") id: Int,
        @Query("search") query: String
    ): Response<List<ShopListResponse>>

    @GET("/shop/shop-map-list/{id}/")
    suspend fun getMapList(
        @Path("id") id: Int,
        @Query("radius") radius: Int
    ): Response<List<MapListResponse>>

}