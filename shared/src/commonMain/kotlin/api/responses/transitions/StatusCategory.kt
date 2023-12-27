package api.responses.transitions


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatusCategory(
    @SerialName("colorName")
    val colorName: String,
    @SerialName("id")
    val id: Int,
    @SerialName("key")
    val key: String,
    @SerialName("name")
    val name: String,
    @SerialName("self")
    val self: String,
)