package project

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProjectConfigPanel(
    project: String,
    team: String,
    totalIssues: Int,
    filteredIssues: Int,
    statusList: List<String>,
    fromStatus: String,
    toStatus: String,

    onProjectChange: (String) -> Unit,
    onTeamChanged: (String) -> Unit,
    onFromStatus: (String) -> Unit,
    onToStatus: (String) -> Unit,
) {

    var fromStatusExpanded: Boolean by remember { mutableStateOf(false) }
    var toStatusExpanded: Boolean by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .border(1.dp, color = Color.Black)
        ) {
            TextField(
                value = project,
                onValueChange = onProjectChange,
                label = { Text("Team") }
            )

            TextField(
                value = team,
                onValueChange = onTeamChanged,
                label = { Text("Team") }
            )

            Text(
                text = "From: $fromStatus",
                modifier = Modifier.clickable { fromStatusExpanded = true }
                    .padding(4.dp)
            )
            DropdownMenu(
                expanded = fromStatusExpanded,
                onDismissRequest = { fromStatusExpanded = false },
                content = {
                    statusList.forEach {
                        DropdownMenuItem(
                            onClick = {
                                onFromStatus(it)
                                fromStatusExpanded = false
                            }
                        ) {
                            Text(it)
                        }
                    }
                }
            )

            Text(
                text = "To: ${toStatus}",
                modifier = Modifier.clickable { toStatusExpanded = true }
                    .padding(4.dp)
            )
            DropdownMenu(
                expanded = toStatusExpanded,
                onDismissRequest = { toStatusExpanded = false },
                content = {
                    statusList.forEach {
                        DropdownMenuItem(
                            onClick = {
                                onToStatus(it)
                                toStatusExpanded = false
                            }
                        ) {
                            Text(it)
                        }
                    }
                }
            )
        }

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
                text = "Number of filtered items: $filteredIssues",
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
