package uz.mrx.arigo.presentation.direction.shop

interface ShopListScreenDirection {

    suspend fun openMapSearchScreen(id: Int, roleId:Int)

    suspend fun openShopDetailScreen(id:Int, roleId: Int)


}