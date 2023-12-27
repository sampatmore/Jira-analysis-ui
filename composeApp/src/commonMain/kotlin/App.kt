import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import api.requests.Changelog
import api.requests.IssueTimeBetween
import api.requests.JiraQuery
import chart.CycleTimeHistogramChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import project.FilteredIssuesStatsPanel
import project.ProjectConfigPanel

@OptIn(ExperimentalKoalaPlotApi::class, ExperimentalMaterialApi::class)
@Composable
fun App() {

    val client = client()
    val changelog = Changelog(client)
    val jiraQuery = JiraQuery(client)

    val project = MutableStateFlow("SO")
    val team = MutableStateFlow("39")

    val teamIssues = team.map {
        if (it.isNotBlank()) {
            jiraQuery.issuesForTeam(teamId = it)
        } else emptyList()
    }

    val statuses = project.map {
        jiraQuery.statusesInProject(it)
    }

    val fromStatus = MutableStateFlow("In Progress")
    val toStatus = MutableStateFlow("Done")

    val daysFromStartOfWork = combine(teamIssues, fromStatus, toStatus) { issues, from, to ->
        changelog.daysBetweenStatuses(
            issueIds = issues,
            fromStatus = from,
            toStatus = to,
        )
    }

    MaterialTheme {

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

            val projectValue: String by project.collectAsState()
            val teamValue: String by team.collectAsState()

            val teamIssuesValue: List<String> by teamIssues.collectAsState(emptyList())
            val daysFromStartOfWorkValue: List<IssueTimeBetween> by daysFromStartOfWork.collectAsState(emptyList())

            val statusList: List<String> by statuses.collectAsState(emptyList())

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                ProjectConfigPanel(
                    project = projectValue,
                    team = teamValue,
                    statusList = statusList,
                    fromStatus = fromStatus.value,
                    toStatus = toStatus.value,

                    onProjectChange = { project.value = it },
                    onTeamChanged = { team.value = it },
                    onFromStatus = { fromStatus.value = it },
                    onToStatus = { toStatus.value = it },
                )

                FilteredIssuesStatsPanel(
                    teamIssues = teamIssuesValue,
                    filteredByStatusTransition = daysFromStartOfWorkValue,
                )
            }

            if (daysFromStartOfWorkValue.isNotEmpty()) {
                CycleTimeHistogramChart(issueTimes = daysFromStartOfWorkValue)
            }
        }
    }
}
