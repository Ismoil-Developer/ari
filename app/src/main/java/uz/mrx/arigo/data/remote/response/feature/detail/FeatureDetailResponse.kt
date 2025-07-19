package uz.mrx.arigo.data.remote.response.feature.detail
data class FeatureDetailResponse(
    val id: Int,
    val image: String,
    val title: String,
    val work_start: String,
    val work_end: String,
    val phone_number: String,
    val rating: Double,
    val about: String,
    val coordinates: Coordinates,
    val is_active: Boolean,
    val role: Int,
    val locations: String,
    val feedbacks: List<Feedback>
)

data class Coordinates(
    val type: String,
    val coordinates: List<Double>
)

data class Feedback(
    val id: Int,
    val user: String,
    val rating: Int,
    val comment: String,
    val created_at: String
)
