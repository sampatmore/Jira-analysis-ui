package api.responses.transitions


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Transition(
    @SerialName("hasScreen")
    val hasScreen: Boolean,
    @SerialName("id")
    val id: String,
    @SerialName("isAvailable")
    val isAvailable: Boolean,
    @SerialName("isConditional")
    val isConditional: Boolean,
    @SerialName("isGlobal")
    val isGlobal: Boolean,
    @SerialName("isInitial")
    val isInitial: Boolean,
    @SerialName("isLooped")
    val isLooped: Boolean,
    @SerialName("name")
    val name: String,
    @SerialName("to")
    val to: To,
)