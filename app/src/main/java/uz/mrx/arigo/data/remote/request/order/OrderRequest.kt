package uz.mrx.arigo.data.remote.request.order

data class OrderRequest(
    private val items:String,
    private val allow_other_shops:Boolean,
    private val additional_shop:Int?
)