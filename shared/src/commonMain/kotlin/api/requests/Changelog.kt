package api.requests

import api.JiraClient
import api.responses.changelog.ChangelogResponse
import jiraInstant
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.Instant

class Changelog(
    private val jiraClient: JiraClient,
) {

    suspend fun daysBetweenStatuses(
        issueIds: List<String>,
        fromStatus: String,
        toStatus: String,
    ): List<IssueTimeBetween> {
        return coroutineScope {
            issueIds.map {
                async { daysBetweenStatuses(it, fromStatus, toStatus) }
            }.awaitAll()
                .filterNotNull()
        }
    }

    suspend fun daysBetweenStatuses(
        issueId: String,
        from: String,
        to: String,
    ): IssueTimeBetween? {
        val transitionLog = statusChangeForIssue(issueId)

        val fromDate = transitionLog.transitions.find { it.to == from }?.date ?: return null
        val toDate = transitionLog.transitions.find { it.to == to }?.date ?: return null

        return IssueTimeBetween(
            issueId = issueId,
            from = from,
            fromDate = fromDate,
            to = to,
            toDate = toDate,
        )
    }

    suspend fun statusChangeForIssue(issueId: String): IssueTransitionLog {
        val response = jiraClient.get<ChangelogResponse>(path = "issue/$issueId/changelog")

        return with(response) {

            IssueTransitionLog(
                issueId = issueId,
                transitions = this.transitions.filter { it.items.first().field == "status" }
                    .map {
                        StatusTransition(
                            date = jiraInstant(it.created),
                            from = it.items.first().fromString.orEmpty(), // can be empty when created
                            to = it.items.first().toString.orEmpty()
                        )
                    }
            )
        }
    }
}

data class IssueTransitionLog(
    val issueId: String,
    val transitions: List<StatusTransition>,
)

data class StatusTransition(
    val date: Instant,
    val from: String,
    val to: String,
)

data class IssueTimeBetween(
    val issueId: String,
    val from: String,
    val fromDate: Instant,
    val to: String,
    val toDate: Instant,
) {

    val wholeDays: Long get() = (toDate - fromDate).inWholeDays
}