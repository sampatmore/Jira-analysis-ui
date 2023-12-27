package api.requests

import api.JiraClient
import api.responses.queryIssueList.QueryIssueListResponse

class JiraQuery(
    private val jiraClient: JiraClient,
) {

    suspend fun issuesForTeam(teamId: String, startAt: Int = 0): List<String> {
        val response = jiraClient.get<QueryIssueListResponse>(path = "search/?jql=team=$teamId&fields=[key]&maxResults=100&startAt=$startAt")

        return with(response) {
            if (startAt + maxResults < total) {
                issueIds + issuesForTeam(teamId, startAt = startAt + maxResults + 1)
            } else {
                issueIds
            }
        }
    }
}