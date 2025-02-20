/*
* Copyright 2020-2023 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
*/

import org.jetbrains.kotlinx.kandy.dsl.continuous
import org.jetbrains.kotlinx.kandy.dsl.invoke
import org.jetbrains.kotlinx.kandy.dsl.plot
import org.jetbrains.kotlinx.kandy.letsplot.export.save
import org.jetbrains.kotlinx.kandy.letsplot.feature.layout
import org.jetbrains.kotlinx.kandy.letsplot.layers.bars
import org.jetbrains.kotlinx.kandy.letsplot.layers.line
import org.jetbrains.kotlinx.kandy.letsplot.settings.LineType
import org.jetbrains.kotlinx.kandy.letsplot.x
import org.jetbrains.kotlinx.kandy.letsplot.y
import org.jetbrains.kotlinx.kandy.util.color.Color

public fun main() {

    val simpleDataset = mapOf(
        "time" to listOf(0, 1, 2, 4, 5, 7, 8, 9),
        "temperature" to listOf(12.0, 14.2, 15.1, 15.9, 17.9, 15.6, 14.2, 24.3),
        "humidity" to listOf(0.5, 0.32, 0.11, 0.89, 0.68, 0.57, 0.56, 0.5)
    )

    val simplePlot = plot(simpleDataset) {

        x("time"<Int>())

        y("temperature"<Double>()) {
            scale = continuous(0.0..25.5)
        }

        bars {
            fillColor("humidity"<Double>()) {
                scale = continuous(
                    range = Color.YELLOW .. Color.RED
                )
            }
            borderLine.width = 0.0
        }

        line {
            width = 3.0
            color = Color.hex("#6e5596")
            type = LineType.DOTDASH
        }

        layout {
            title = "Simple plot with lets-plot"
            caption = "See `examples` section for more\n complicated and interesting examples!"
        }

    }

    simplePlot.save("lets_plot_simple.png")
}
