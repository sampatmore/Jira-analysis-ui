import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import api.requests.Changelog
import api.requests.JiraQuery
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {

    val client = client()
    val changelog = Changelog(client())
    val jiraQuery = JiraQuery(client())

    val team = MutableStateFlow("")

    val teamIssues = team.map {
        if (it.isNotBlank()) {
            jiraQuery.issuesForTeam(teamId = "39")
        } else emptyList()
    }

    val daysFromStartOfWork = teamIssues.map {
        changelog.daysBetweenStatuses(
            issueIds = it,
            fromStatus = "In Progress",
            toStatus = "Done",
        )
    }


//    val frequencyMap = daysFromStartOfWork.groupingBy { it }.eachCount().toSortedMap()
//    println("Distribution: $frequencyMap")

    MaterialTheme {

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

            val teamValue by team.collectAsState()
            TextField(
                value = teamValue,
                onValueChange = {
                    team.value = it
                },
                label = { Text("Team") }
            )

            val teamIssuesValue by teamIssues.collectAsState(emptyList())
            Text("Number of items in team: ${teamIssuesValue.size}")

            val daysFromStartOfWorkValue by daysFromStartOfWork.collectAsState(emptyList())
            Text("Number of items completed after start of work: ${daysFromStartOfWorkValue.size}")
            println("Average number of days to complete: ${daysFromStartOfWorkValue.average()}")

            //    val frequencyMap = daysFromStartOfWork.groupingBy { it }.eachCount().toSortedMap()

        }
    }
}