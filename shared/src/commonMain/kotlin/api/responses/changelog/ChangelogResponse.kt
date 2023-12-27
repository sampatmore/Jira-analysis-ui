package api.responses.changelog


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangelogResponse(
//    @SerialName("isLast")
//    val isLast: Boolean,
//    @SerialName("maxResults")
//    val maxResults: Int,
//    @SerialName("self")
//    val self: String,
//    @SerialName("startAt")
//    val startAt: Int,
//    @SerialName("total")
//    val total: Int,
    @SerialName("values")
    val transitions: List<Transition>,
)