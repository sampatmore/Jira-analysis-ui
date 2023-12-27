package api.responses.transitions


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class To(
    @SerialName("description")
    val description: String,
    @SerialName("iconUrl")
    val iconUrl: String,
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("self")
    val self: String,
    @SerialName("statusCategory")
    val statusCategory: StatusCategory,
)