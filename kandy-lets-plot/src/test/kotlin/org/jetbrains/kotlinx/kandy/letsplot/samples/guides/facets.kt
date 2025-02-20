package org.jetbrains.kotlinx.kandy.letsplot.samples.guides

import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.ParserOptions
import org.jetbrains.kotlinx.dataframe.api.column
import org.jetbrains.kotlinx.dataframe.api.head
import org.jetbrains.kotlinx.dataframe.io.readCSV
import org.jetbrains.kotlinx.kandy.dsl.invoke
import org.jetbrains.kotlinx.kandy.dsl.plot
import org.jetbrains.kotlinx.kandy.letsplot.feature.layout
import org.jetbrains.kotlinx.kandy.letsplot.layers.points
import org.jetbrains.kotlinx.kandy.letsplot.multiplot.facet.*
import org.jetbrains.kotlinx.kandy.letsplot.samples.SampleHelper
import org.jetbrains.kotlinx.kandy.letsplot.style.Style
import org.jetbrains.kotlinx.kandy.letsplot.x
import org.jetbrains.kotlinx.kandy.letsplot.y
import java.util.*
import kotlin.test.Test

class Facets : SampleHelper("multiplot", "guides") {

    private val dataset = DataFrame.readCSV(
        "https://raw.githubusercontent.com/JetBrains/lets-plot-kotlin/master/docs/examples/data/mpg2.csv",
        parserOptions = ParserOptions(Locale.ENGLISH)
    )
    private val `engine horsepower` = column<Int>("engine horsepower")
    private val `miles per gallon` = column<Double>("miles per gallon")
    private val `origin of car` = column<String>("origin of car")
    private val `number of cylinders` = column<Int>("number of cylinders")

    @Test
    fun guideFacetsReadDataFrame() {
        // SampleStart
        val dataset = DataFrame.readCSV(
            "https://raw.githubusercontent.com/JetBrains/lets-plot-kotlin/master/docs/examples/data/mpg2.csv",
            parserOptions = ParserOptions(Locale.ENGLISH)
        )

        dataset.head(3)
        // SampleEnd
    }

    @Test
    fun guideFacetsScatterPlotByHorsePower() {
        // SampleStart
        dataset.plot {
            x(`engine horsepower`)
            y(`miles per gallon`)
            points {
                color(`origin of car`)
            }
            layout {
                style(Style.Grey)
                size = 800 to 350
            }
        }
            // SampleEnd
            .saveSample()
    }

    @Test
    fun guideFacetsGridXPlot() {
        // SampleStart
        dataset.plot {
            points {
                x(`engine horsepower`)
                y(`miles per gallon`)
                color(`origin of car`)
            }
            layout {
                style(Style.Grey)
            }
            facetGridX(`number of cylinders`)
        }
            // SampleEnd
            .saveSample()
    }

    @Test
    fun guideFacetsTwoPlotsGrid() {
        // SampleStart
        dataset.plot {
            points {
                x(`engine horsepower`)
                y(`miles per gallon`)
                color(`origin of car`)
            }
            layout {
                style(Style.Grey)
            }
            facetGrid(`number of cylinders`, `origin of car`)
        }
            // SampleEnd
            .saveSample()
    }

    @Test
    fun guideFacetsWithFormattingAndSorting() {
        // SampleStart
        dataset.plot {
            points {
                x(`engine horsepower`)
                y(`miles per gallon`)
                color(`origin of car`)
            }
            layout {
                style(Style.Grey)
            }
            facetGrid(`number of cylinders`, `origin of car`, xFormat = "{d} cyl", yOrder = OrderDirection.DESCENDING)
        }
            // SampleEnd
            .saveSample()
    }

    @Test
    fun guideFacetsWrapOnePlot() {
        // SampleStart
        dataset.plot {
            points {
                x(`engine horsepower`)
                y(`miles per gallon`)
                color(`origin of car`)
            }
            layout {
                style(Style.Grey)
            }
            facetWrap(nRow = 2) {
                facet(`number of cylinders`)
            }
        }
            // SampleEnd
            .saveSample()
    }

    @Test
    fun guideFacetsWrapTwoPlots() {
        // SampleStart
        dataset.plot {
            points {
                x(`engine horsepower`)
                y(`miles per gallon`)
                color(`origin of car`)
            }
            layout {
                style(Style.Grey)
            }
            facetWrap(nCol = 5) {
                facet(`origin of car`)
                facet(`number of cylinders`)
            }
        }
            // SampleEnd
            .saveSample()
    }

    @Test
    fun guideFacetsArrangeVertically() {
        // SampleStart
        dataset.plot {
            points {
                x(`engine horsepower`)
                y(`miles per gallon`)
                color(`origin of car`)
            }
            layout {
                style(Style.Grey)
            }
            facetWrap(nCol = 3, direction = Direction.VERTICAL) {
                facet(`origin of car`, OrderDirection.ASCENDING, null)
                facet(`number of cylinders`, OrderDirection.DESCENDING, "{} cyl")
            }
        }
            // SampleEnd
            .saveSample()
    }

    @Test
    fun guideFreeFacetSimplePlot() {
        // SampleStart
        dataset.plot {
            x(`engine horsepower`)
            y("engine displacement (cu. inches)"<Double>())
            points {
                color(`origin of car`)
            }
            layout {
                style(Style.Grey)
                size = 800 to 350
            }
        }
            // SampleEnd
            .saveSample()
    }

    @Test
    fun guideFreeFacetWithFixedScales() {
        // SampleStart
        dataset.plot {
            x(`engine horsepower`)
            y("engine displacement (cu. inches)"<Double>())
            points {
                color(`origin of car`)
            }
            layout {
                style(Style.Grey)
                size = 800 to 500
            }
            facetGridY(`origin of car`)
        }
        // SampleEnd
            .saveSample()
    }

    @Test
    fun guideFreeFacetGridYFree() {
        // SampleStart
        dataset.plot {
            x(`engine horsepower`)
            y("engine displacement (cu. inches)"<Double>())
            points {
                color(`origin of car`)
            }
            layout {
                style(Style.Grey)
                size = 800 to 500
            }
            facetGridY(`origin of car`, scalesSharing = ScalesSharing.FREE_Y)
        }
        // SampleEnd
            .saveSample()
    }

    @Test
    fun guideFreeFacetWrapWithFixedScale() {
        // SampleStart
        dataset.plot {
            x(`engine horsepower`)
            y("engine displacement (cu. inches)"<Double>())
            points {
                color(`origin of car`)
            }
            layout {
                style(Style.Grey)
                size = 800 to 500
            }
            facetWrap {
                facet(`number of cylinders`, order = OrderDirection.ASCENDING)
            }
        }
            // SampleEnd
            .saveSample()
    }

    @Test
    fun guideFreeFacetWrapWithFreeScale() {
        // SampleStart
        dataset.plot {
            x(`engine horsepower`)
            y("engine displacement (cu. inches)"<Double>())
            points {
                color(`origin of car`)
            }
            layout {
                style(Style.Grey)
                size = 800 to 500
            }
            facetWrap(scalesSharing = ScalesSharing.FREE) {
                facet(`number of cylinders`, order = OrderDirection.ASCENDING)
            }
        }
            // SampleEnd
            .saveSample()
    }
}

