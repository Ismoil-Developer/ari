package uz.mrx.arigo.data.remote.request.location


data class LocationCreateRequest(
    val custom_name:String,
    val coordinates: String,
    val address:String,
    val active:Boolean
)