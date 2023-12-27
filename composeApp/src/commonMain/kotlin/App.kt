import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import api.requests.Changelog
import api.requests.JiraQuery
import chart.CycleTimeHistogramChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

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

            val projectValue by team.collectAsState()
            val teamValue by team.collectAsState()

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .border(1.dp, color = Color.Black)
                ) {
                    TextField(
                        value = projectValue,
                        onValueChange = {
                            project.value = it
                        },
                        label = { Text("Team") }
                    )

                    TextField(
                        value = teamValue,
                        onValueChange = {
                            team.value = it
                        },
                        label = { Text("Team") }
                    )


                    val statusList by statuses.collectAsState(emptyList())

                    var fromStatusExpanded by remember { mutableStateOf(false) }
                    Text(
                        text = "To: ${fromStatus.value}",
                        modifier = Modifier.clickable { fromStatusExpanded = true }
                    )
                    DropdownMenu(
                        expanded = fromStatusExpanded,
                        onDismissRequest = { fromStatusExpanded = false },
                        content = {
                            statusList.forEach {
                                DropdownMenuItem(
                                    onClick = {
                                        fromStatus.value = it
                                        fromStatusExpanded = false
                                    }
                                ) {
                                    Text(it)
                                }
                            }
                        }
                    )

                    var toStatusExpanded by remember { mutableStateOf(false) }
                    Text(
                        text = "To: ${toStatus.value}",
                        modifier = Modifier.clickable { toStatusExpanded = true }
                    )
                    DropdownMenu(
                        expanded = toStatusExpanded,
                        onDismissRequest = { toStatusExpanded = false },
                        content = {
                            statusList.forEach {
                                DropdownMenuItem(
                                    onClick = {
                                        toStatus.value = it
                                        toStatusExpanded = false
                                    }
                                ) {
                                    Text(it)
                                }
                            }
                        }
                    )
                }
            }

            val teamIssuesValue by teamIssues.collectAsState(emptyList())
            Text("Number of items in team: ${teamIssuesValue.size}")

            val daysFromStartOfWorkValue by daysFromStartOfWork.collectAsState(emptyList())


            if (daysFromStartOfWorkValue.isNotEmpty()) {
                CycleTimeHistogramChart(issueTimes = daysFromStartOfWorkValue)
            }
        }
    }
}
