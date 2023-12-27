package api.responses.queryIssueList


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QueryIssueListResponse(
    @SerialName("issues")
    val issues: List<Issue>,
    @SerialName("maxResults")
    val maxResults: Int,
    @SerialName("startAt")
    val startAt: Int,
    @SerialName("total")
    val total: Int,
) {

    val issueIds: List<String>
        get() = issues.map { it.id }
}