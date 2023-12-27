package api.responses.queryStatuses


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QueryStatusesResponseItem(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("self")
    val self: String,
    @SerialName("statuses")
    val statuses: List<Statuse>,
    @SerialName("subtask")
    val subtask: Boolean
)