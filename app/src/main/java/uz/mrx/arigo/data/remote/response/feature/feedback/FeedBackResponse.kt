package uz.mrx.arigo.data.remote.response.feature.feedback

data class FeedBackResponse(
    val id:Int,
    val user:String,
    val rating:Int,
    val comment:String,
    val created_at:String
)
