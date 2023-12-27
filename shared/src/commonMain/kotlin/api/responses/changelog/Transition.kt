package api.responses.changelog


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Transition(
    @SerialName("author")
    val author: Author,
    @SerialName("created")
    val created: String,
    @SerialName("id")
    val id: String,
    @SerialName("items")
    val items: List<Item>,
)