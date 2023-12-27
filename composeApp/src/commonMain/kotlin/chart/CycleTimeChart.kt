package chart

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.bar.DefaultVerticalBar
import io.github.koalaplot.core.bar.VerticalBarPlot
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.LinearAxisModel
import io.github.koalaplot.core.xygraph.XYGraph

@ExperimentalKoalaPlotApi
@Composable
fun CycleTimeHistogramChart(
    frequencyMap: Map<Int, Int>,
) {
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