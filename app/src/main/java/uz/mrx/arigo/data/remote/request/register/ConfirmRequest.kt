package uz.mrx.arigo.data.remote.request.register


data class ConfirmRequest(
    val phone_number:String,
    val code:String
)