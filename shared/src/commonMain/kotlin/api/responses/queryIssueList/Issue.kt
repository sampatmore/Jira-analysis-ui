package api.responses.queryIssueList


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Issue(
    @SerialName("expand")
    val expand: String,
    @SerialName("id")
    val id: String,
    @SerialName("key")
    val key: String,
    @SerialName("self")
    val self: String
)