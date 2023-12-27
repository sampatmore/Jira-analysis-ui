package chart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.RangeSlider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import api.requests.IssueTimeBetween
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.bar.DefaultVerticalBar
import io.github.koalaplot.core.bar.VerticalBarPlot
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.LinearAxisModel
import io.github.koalaplot.core.xygraph.XYGraph
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@ExperimentalMaterialApi
@ExperimentalKoalaPlotApi
@Composable
fun CycleTimeHistogramChart(
    modifier: Modifier = Modifier,
    issueTimes: List<IssueTimeBetween>,
) {

    val dateRangeStart = issueTimes.minOf { it.fromDate }
    val dateRangeEnd = issueTimes.maxOf { it.toDate }

    val slider = remember { MutableStateFlow(0f..1f) }

    val dateStart = slider.map {
        dateRangeStart + (dateRangeEnd - dateRangeStart) * it.start.toDouble()
    }

    val dateEnd = slider.map {
        dateRangeStart + (dateRangeEnd - dateRangeStart) * it.start.toDouble()
    }

    val dateRange = slider.map {
        val range = (dateRangeEnd - dateRangeStart)
        dateRangeStart + range * it.start.toDouble()..dateRangeStart + range * it.endInclusive.toDouble()
    }

    if (issueTimes.isNotEmpty()) {

        Column(
            modifier = modifier,
        ) {
            val dateRangeValue by dateRange.collectAsState(null)

            Row {
                Text("Start\n${dateRangeValue?.start?.toLocalDateTime(TimeZone.UTC)?.date}")
                RangeSlider(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    value = slider.value,
                    onValueChange = {
                        slider.value = it
                    },
                )
                Text("End\n${dateRangeValue?.endInclusive?.toLocalDateTime(TimeZone.UTC)?.date}")
            }

            val frequencyMap by dateRange.map {range ->
                issueTimes.filter { it.fromDate in range }
                    .groupingBy { it.wholeDays }
                    .eachCount()
                    .toCycleTimeHistogram()
            }.collectAsState(emptyMap())

            if (frequencyMap.isNotEmpty()) {
                CycleTimeHistogramChart(
                    frequencyMap = frequencyMap,
                )
            }
        }
    } else {
        Text("No values to display")
    }
}

@ExperimentalKoalaPlotApi
@Composable
fun CycleTimeHistogramChart(
    modifier: Modifier = Modifier,
    frequencyMap: Map<Int, Int>,
) {
    ChartLayout(title = { Text("Cycle time distribution") }) {

        XYGraph(
            modifier = modifier,
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

fun Map<Long, Int>.toCycleTimeHistogram(): Map<Int, Int> {
    if (isEmpty()) return emptyMap()

    val map = HashMap<Int, Int>()

    var i = 0L
    do {
        map[i.toInt()] = getOrElse(i) { 0 }
        i++
    } while (i <= keys.max())

    return map
}
