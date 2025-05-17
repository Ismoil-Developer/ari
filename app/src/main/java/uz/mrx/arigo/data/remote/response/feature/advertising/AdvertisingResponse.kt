package uz.mrx.arigo.data.remote.response.feature.advertising

data class AdvertisingResponse(
    val id: Int,
    val image: String,
    val title: String,
    val text: String,
    val link: String,
    val created_at: String
)
