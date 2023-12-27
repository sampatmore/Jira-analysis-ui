package api.responses.queryStatuses


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Statuse(
    @SerialName("description")
    val description: String,
    @SerialName("iconUrl")
    val iconUrl: String,
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("self")
    val self: String
)