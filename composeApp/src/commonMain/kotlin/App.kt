import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import api.requests.Changelog
import api.requests.JiraQuery
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.bar.DefaultVerticalBar
import io.github.koalaplot.core.bar.VerticalBarPlot
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.LinearAxisModel
import io.github.koalaplot.core.xygraph.XYGraph
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class, ExperimentalKoalaPlotApi::class)
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

            val frequencyMap = daysFromStartOfWorkValue.groupingBy { it }.eachCount().toHistogram()

            if (frequencyMap.isNotEmpty()) {
                ChartLayout(title = { Text("Cycle time distribution") }) {

                    XYGraph(
                        xAxisModel = LinearAxisModel(range = 0f..frequencyMap.keys.max().toFloat()),
                        yAxisModel = LinearAxisModel(range = 0f..frequencyMap.values.max().toFloat()),//: AxisModel<Y>,
                    ) {
                        VerticalBarPlot(
                            xData = frequencyMap.keys.map { it.toFloat() },
                            yData = frequencyMap.values.map { it.toFloat() },
                            barWidth = 0.9f,
                            bar = {
                                DefaultVerticalBar(
                                    brush = SolidColor(Color.Blue),
                                    modifier = Modifier.fillMaxWidth(0.9f),
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

fun Map<Long, Int>.toHistogram(): Map<Int, Int> {
    if (isEmpty()) return emptyMap()

    val map = HashMap<Int, Int>()

    var i = 0L
    do {
        map[i.toInt()] = getOrElse(i) { 0 }
        i++
    } while (i <= keys.max())

    return map
}