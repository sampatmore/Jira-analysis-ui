package project

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import api.requests.IssueTimeBetween

@Composable
fun FilteredIssuesStatsPanel(
    teamIssues: List<String>,
    filteredByStatusTransition: List<IssueTimeBetween>,
) {

    val totalIssues = teamIssues.size
    val totalFiltered = filteredByStatusTransition.size

    Column(
        modifier = Modifier
            .padding(8.dp)
            .border(1.dp, color = Color.Black)
    ) {
        Text(
            text = "Total number of items: $totalIssues",
            modifier = Modifier.padding(4.dp)
        )
        Text(
            text = "Number of filtered items: $totalFiltered",
            modifier = Modifier.padding(4.dp)
        )
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .border(1.dp, color = Color.Black)
    ) {

        if (filteredByStatusTransition.isEmpty()) return

        val sortedLengths = filteredByStatusTransition.map { it.wholeDays }.sorted()
        val percentile50 = sortedLengths[(5 * sortedLengths.size / 10f).toInt()]
        val percentile70 = sortedLengths[(7 * sortedLengths.size / 10f).toInt()]
        val percentile85 = sortedLengths[(8.5 * sortedLengths.size / 10f).toInt()]
        val percentile90 = sortedLengths[(9 * sortedLengths.size / 10f).toInt()]

        Text(
            text = "50% completed in: $percentile50 days",
            modifier = Modifier.padding(4.dp)
        )
        Text(
            text = "70% completed in: $percentile70 days",
            modifier = Modifier.padding(4.dp)
        )
        Text(
            text = "85% completed in: $percentile85 days",
            modifier = Modifier.padding(4.dp)
        )
        Text(
            text = "90% completed in: $percentile90 days",
            modifier = Modifier.padding(4.dp)
        )
    }
}
