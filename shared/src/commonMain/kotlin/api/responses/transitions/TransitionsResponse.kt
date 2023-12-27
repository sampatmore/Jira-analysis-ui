package api.responses.transitions


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransitionsResponse(
    @SerialName("expand")
    val expand: String,
    @SerialName("transitions")
    val transitions: List<Transition>,
)