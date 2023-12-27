package api.requests

import api.JiraClient
import api.responses.changelog.ChangelogResponse
import jiraInstant
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

class Changelog(
    private val jiraClient: JiraClient,
) {

    suspend fun daysBetweenStatuses(
        issueIds: List<String>,
        fromStatus: String,
        toStatus: String,
    ): List<Long> {
        return coroutineScope {
            issueIds.map {
                async { daysBetweenStatuses(it, fromStatus, toStatus) }
            }.awaitAll()
                .filterNotNull()
        }
    }

    suspend fun daysBetweenStatuses(
        issueId: String,
        fromStatus: String,
        toStatus: String,
    ): Long? {
        val transitionLog = statusChangeForIssue(issueId)

        val from = transitionLog.transitions.find { it.to == fromStatus }?.date ?: return null
        val to = transitionLog.transitions.find { it.to == toStatus }?.date ?: return null

        return (to - from).inWholeDays
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