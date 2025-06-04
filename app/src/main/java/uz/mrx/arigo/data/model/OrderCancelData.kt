package uz.mrx.arigo.data.model

data class OrderCancelData(
    val id: Int,
    val reason: String,
    var isSelected: Boolean = false
)
