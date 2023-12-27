package api.responses.changelog


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerialName("field")
    val `field`: String,
    @SerialName("fromString")
    val fromString: String?,
    @SerialName("toString")
    val toString: String?,
)