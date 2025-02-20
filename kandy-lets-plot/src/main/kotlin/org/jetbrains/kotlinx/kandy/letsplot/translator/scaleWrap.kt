/*
* Copyright 2020-2023 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
*/

@file:Suppress("UNCHECKED_CAST")

package org.jetbrains.kotlinx.kandy.letsplot.translator

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import org.jetbrains.kotlinx.kandy.ir.bindings.Mapping
import org.jetbrains.kotlinx.kandy.ir.scale.*
import org.jetbrains.kotlinx.kandy.letsplot.internal.*
import org.jetbrains.kotlinx.kandy.letsplot.scales.*
import org.jetbrains.kotlinx.kandy.letsplot.scales.guide.LegendType
import org.jetbrains.kotlinx.kandy.letsplot.scales.guide.model.Axis
import org.jetbrains.kotlinx.kandy.letsplot.scales.guide.model.Legend
import org.jetbrains.kotlinx.kandy.letsplot.scales.guide.model.ScaleParameters
import org.jetbrains.kotlinx.kandy.letsplot.settings.LineType
import org.jetbrains.kotlinx.kandy.letsplot.settings.Symbol
import org.jetbrains.kotlinx.kandy.util.color.Color
import org.jetbrains.letsPlot.core.plot.base.Aes
import org.jetbrains.letsPlot.core.spec.Option
import org.jetbrains.letsPlot.intern.Options
import org.jetbrains.letsPlot.scale.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf

private typealias Aesthetic = org.jetbrains.kotlinx.kandy.ir.aes.Aes

internal val dateTimeTypes = setOf(
    typeOf<Instant>(), typeOf<LocalDateTime>(), typeOf<LocalDate>(),
    typeOf<Instant?>(), typeOf<LocalDateTime?>(), typeOf<LocalDate?>()
)

internal val discreteAes = setOf(
    SHAPE, LINE_TYPE
)

internal fun List<*>?.wrap() = this?.filterNotNull()
internal val timeTypes = setOf(
    typeOf<LocalTime>(), typeOf<LocalTime?>(),
)

internal fun Mapping.wrapScale(domainType: KType, groupKeys: List<String>?): org.jetbrains.letsPlot.intern.Scale? {
    return parameters?.scale?.wrap(
        aes, domainType,
        (parameters as? LetsPlotPositionalMappingParameters<*>)?.axis
            ?: (parameters as? LetsPlotNonPositionalMappingParameters<*, *>)?.legend,
        groupKeys?.contains(columnID) == true
    )
}

/**
 * TODO datetime
 */
internal fun Scale.wrap(
    aes: Aesthetic,
    domainType: KType,
    scaleParameters: ScaleParameters?,
    isGroupKey: Boolean,
): org.jetbrains.letsPlot.intern.Scale {
    return when (this) {

        is PositionalScale<*> -> {
            val naValue = if (this is ContinuousScale<*>) {
                wrapValue(nullValue)
            } else {
                null //(this as PositionalCategoricalScale<*>).c
            }
            val axis = scaleParameters as? Axis<*>?

            val name = axis?.name
            val breaks = axis?.breaks
            val labels = axis?.labels
            val format = axis?.format
            val expand = axis?.expand

            // todo discrete datetime
            /*
            if (domainType in dateTimeTypes) {
                return when (aes) {
                    X -> scaleXDateTime(
                        //  limits = limits.toLP(),
                        name = name,
                        breaks = breaks?.map { it as Number }, // TODO() }
                        labels = labels,
                        //   trans = (transform as? Transformation)?.name,
                        format = format
                    )

                    Y -> scaleYDateTime(
                        //limits = categories,
                        name = name,
                        breaks = breaks,
                        labels = labels,
                        format = format
                    )

                    else -> TODO()
                }
            }

             */

            when (this) {
                is PositionalCategoricalScale<*> -> {
                    when (aes) {
                        X -> scaleXDiscrete(
                            limits = categories?.wrap(),
                            name = name,
                            breaks = breaks?.wrap(),
                            labels = labels,
                            format = format,
                            expand = expand
                            // naValue = nullValue as? Number
                        )

                        Y -> scaleYDiscrete(
                            limits = categories?.wrap(),
                            name = name,
                            breaks = breaks?.wrap(),
                            labels = labels,
                            format = format,
                            expand = expand
                            //  naValue = nullValue as? Number
                        )

                        else -> TODO("error")
                    }
                }

                is PositionalContinuousScale<*> -> {
                    when (aes) {
                        X -> if (domainType in dateTimeTypes) {
                            scaleXDateTime(
                                limits = (min to max).wrap(),
                                name = name,
                                breaks = breaks?.filterNotNull(), // todo
                                labels = labels,
                                format = format,
                                expand = expand
                            )
                        } else if (domainType in timeTypes) {
                            scaleXTime(
                                limits = (min to max).wrap(),
                                name = name,
                                breaks = breaks?.filterNotNull(), // todo
                                labels = labels,
                                expand = expand
                                // format = format
                            )
                        } else {
                            scaleXContinuous(
                                limits = (min to max).wrap(),
                                name = name,
                                breaks = breaks?.map { it as Number }, // TODO() }
                                labels = labels,
                                trans = (transform as? Transformation)?.name,
                                format = format,
                                expand = expand,
                                naValue = naValue as? Number
                            )
                        }

                        Y -> if (domainType in dateTimeTypes) {
                            scaleYDateTime(
                                limits = (min to max).wrap(),
                                name = name,
                                breaks = breaks?.wrap(),
                                labels = labels,
                                format = format,
                                expand = expand,
                                naValue = naValue
                            )
                        } else if (domainType in timeTypes) {
                            scaleYTime(
                                limits = (min to max).wrap(),
                                name = name,
                                breaks = breaks?.filterNotNull(), // todo
                                labels = labels,
                                expand = expand,
                                // format = format
                            )
                        } else {
                            scaleYContinuous(
                                limits = (min to max).wrap(),
                                name = name,
                                breaks = breaks?.map { it as Number }, // TODO() }
                                labels = labels,
                                trans = (transform as? Transformation)?.name,
                                format = format,
                                expand = expand,
                                naValue = naValue as? Number
                            )
                        }

                        else -> TODO()
                    }
                }

                is PositionalDefaultScale<*> -> if (domainType.isCategoricalType() || isGroupKey) {
                    PositionalCategoricalScale<String>(null).wrap(aes, domainType, scaleParameters, isGroupKey)
                } else {
                    PositionalContinuousScale<Double>(null, null, null, null).wrap(
                        aes,
                        domainType,
                        scaleParameters,
                        isGroupKey
                    )
                }
            }
        }

        is NonPositionalScale<*, *> -> {
            val naValue = if (this is ContinuousScale<*>) {
                wrapValue(nullValue)
            } else {
                // todo custom scales
                (this as? NonPositionalCategoricalScale<*, *>)?.domainCategories?.indexOf(null)?.let {
                    if (it == -1) {
                        null
                    } else rangeValues?.get(it)?.let {
                        wrapValue(it)
                    }
                }
                //null //(this as PositionalCategoricalScale<*>).c
            }
            //val naValue = nullValue?.wrap()
            val legend = scaleParameters as? Legend<*, *>?

            val name = legend?.name
            val breaks = legend?.breaks
            val labels = legend?.labels
            val format = legend?.format
            val legendType = legend?.type?.let {
                when (it) {
                    is LegendType.None -> "none"
                    is LegendType.ColorBar -> guideColorbar(
                        barHeight = it.barHeight,
                        barWidth = it.barWidth,
                        nbin = it.nBin
                    )
                    is LegendType.DiscreteLegend -> guideLegend(
                        nrow = it.nRow,
                        ncol = it.nCol,
                        byRow = it.byRow
                    )
                }
            }

            when (this) {
                is NonPositionalDefaultScale<*, *> -> if (
                    this is NonPositionalDefaultCategoricalScale<*, *> ||
                    domainType.isCategoricalType() || aes in discreteAes || isGroupKey) {
                    NonPositionalCategoricalScale<String, String>(null, null).wrap(
                        aes,
                        domainType,
                        scaleParameters,
                        isGroupKey
                    )
                } else {
                    NonPositionalContinuousScale<Double, Double>(
                        null, null, null, null, null, null
                    ).wrap(
                        aes,
                        domainType,
                        scaleParameters,
                        isGroupKey
                    )
                }

                is NonPositionalCategoricalScale<*, *> -> {
                    when (aes) {
                        SIZE -> if (rangeValues != null) {
                            scaleSizeManual(
                                values = rangeValues!!.map { it as Number },
                                limits = domainCategories?.wrap(),
                                name = name,
                                breaks = breaks?.wrap(),
                                labels = labels,
                                guide = legendType,
                                format = format,
                                naValue = naValue as? Number
                            )
                        } else {
                            org.jetbrains.letsPlot.intern.Scale(
                                Aes.SIZE,
                                limits = domainCategories?.wrap(),
                                name = name,
                                breaks = breaks?.wrap(),
                                labels = labels,
                                guide = legendType,
                                format = format,
                                naValue = naValue as? Number
                            )
                        }

                        COLOR -> {
                            if (rangeValues == null) {
                                scaleColorDiscrete(
                                    limits = domainCategories?.wrap(),
                                    name = name,
                                    breaks = breaks?.wrap(),
                                    labels = labels,
                                    guide = legendType,
                                    format = format,
                                    naValue = naValue
                                )
                            } else {
                                scaleColorManual(
                                    limits = domainCategories?.wrap(),
                                    values = rangeValues!!.map { (it as Color).wrap() }, //todo
                                    name = name,
                                    breaks = breaks?.wrap(),
                                    labels = labels,
                                    guide = legendType,
                                    format = format,
                                    naValue = naValue
                                )
                            }
                        }

                        FILL -> {
                            if (rangeValues == null) {
                                scaleFillDiscrete(
                                    limits = domainCategories?.wrap(),
                                    name = name,
                                    breaks = breaks?.wrap(),
                                    labels = labels,
                                    guide = legendType,
                                    format = format,
                                    naValue = naValue
                                )
                            } else {
                                scaleFillManual(
                                    limits = domainCategories?.wrap(),
                                    values = rangeValues!!.map { (it as Color).wrap() },
                                    name = name,
                                    breaks = breaks?.wrap(),
                                    labels = labels,
                                    guide = legendType,
                                    format = format,
                                    naValue = naValue
                                )
                            }
                        }
                        // TODO

                        ALPHA -> if (rangeValues != null) {
                            scaleAlphaManual(
                                limits = domainCategories?.wrap(),
                                values = rangeValues!!.map { it as Double },
                                name = name,
                                breaks = breaks?.wrap(),
                                labels = labels,
                                guide = legendType,
                                format = format,
                                naValue = naValue as? Number
                            )
                        } else {
                            org.jetbrains.letsPlot.intern.Scale(
                                Aes.ALPHA,
                                limits = domainCategories?.wrap(),
                                name = name,
                                breaks = breaks?.wrap(),
                                labels = labels,
                                guide = legendType,
                                format = format,
                                naValue = naValue
                            )
                        }

                        LINE_TYPE -> if (rangeValues != null) {
                            scaleLinetypeManual(
                                limits = domainCategories?.wrap(),
                                values = rangeValues!!.map { (it as LineType).codeNumber },
                                name = name,
                                breaks = breaks?.wrap(),
                                labels = labels,
                                guide = legendType,
                                format = format,
                                naValue = naValue
                            )
                            // TODO
                        } else {
                            org.jetbrains.letsPlot.intern.Scale(
                                Aes.LINETYPE,
                                limits = domainCategories?.wrap(),
                                name = name,
                                breaks = breaks?.wrap(),
                                labels = labels,
                                guide = legendType,
                                format = format,
                                naValue = naValue
                            )
                        }

                        SHAPE -> if (rangeValues == null) {
                            scaleShape(
                                limits = domainCategories?.wrap(),
                                name = name,
                                breaks = breaks?.wrap(),
                                labels = labels,
                                guide = legendType,
                                format = format,
                                naValue = naValue
                            )
                        } else {
                            scaleShapeManual(
                                limits = domainCategories?.wrap(),
                                values = rangeValues!!.map { (it as Symbol).shape },

                                name = name,
                                breaks = breaks?.wrap(),
                                labels = labels,
                                guide = legendType,
                                format = format,
                                naValue = naValue
                            )
                        }

                        else -> TODO()
                    }
                }

                is NonPositionalContinuousScale<*, *> -> {
                    when (aes) {

                        SIZE -> {
                            // todo
                            val range: Pair<Double, Double>? = if (rangeMin == null && rangeMax == null) {
                                null
                            } else {
                                if (rangeMax == null) {
                                    (rangeMin as Double).let {
                                        it to it + 7.0
                                    }
                                } else if (rangeMin == null) {
                                    (rangeMax as Double).let {
                                        (it - 7.0).coerceAtLeast(0.2) to it
                                    }
                                } else {
                                    rangeMin as Double to rangeMax as Double
                                }
                            }
                            scaleSize(
                                limits = (domainMin to domainMax).wrap(),
                                range = range,//(rangeMin to rangeMax).wrap() as Pair<Number, Number>?, // todo!!
                                name = name,
                                breaks = breaks?.map { it as Number },
                                labels = labels,
                                guide = legendType,
                                trans = (transform as Transformation?)?.name,
                                format = format,
                                naValue = naValue as? Number
                            )
                        }


                        COLOR -> {
                            val lowColor = (rangeMin as? Color)?.wrap()
                            val highColor = (rangeMax as? Color)?.wrap()

                            val limits = (domainMin to domainMax).wrap() // todo datetime support here

                            /*scaleColorContinuous(
                                //low = lowColor,
                                //high = highColor,
                                limits = limits,
                                name = name,
                                breaks = breaks?.map { it as Number },
                                labels = labels,
                                guide = legendType,
                                trans = (transform as Transformation?)?.name,
                                format = format,
                                naValue = naValue

                            )*/

                            org.jetbrains.letsPlot.intern.Scale(
                                aesthetic = Aes.COLOR,
                                name = name,
                                breaks = breaks?.map { it.toString() },
                                labels = labels,
                                limits = limits,
                                naValue = naValue,
                                format = format,
                                guide = legendType,
                                trans = (transform as Transformation?)?.name,
                                otherOptions = Options(
                                    mapOf(
                                        Option.Scale.LOW to lowColor,
                                        Option.Scale.HIGH to highColor,
                                        Option.Scale.SCALE_MAPPER_KIND to Option.Scale.MapperKind.COLOR_GRADIENT
                                    )
                                )
                            )

                        }

                        FILL -> {
                            val lowColor = (rangeMin as? Color)?.wrap()
                            val highColor = (rangeMax as? Color)?.wrap()
                            val limits = (domainMin to domainMax).wrap() //todo datetime support here

                            /*scaleFillContinuous(
                                //low = lowColor,
                                //high = highColor,
                                limits = limits,
                                name = name,
                                breaks = breaks?.map { it as Number },
                                labels = labels,
                                guide = legendType,
                                trans = (transform as Transformation?)?.name,
                                format = format,
                                naValue = naValue
                            )*/

                            org.jetbrains.letsPlot.intern.Scale(
                                aesthetic = Aes.FILL,
                                name = name,
                                breaks = breaks?.map { it.toString() },
                                labels = labels,
                                limits = limits,
                                naValue = naValue,
                                format = format,
                                guide = legendType,
                                trans = (transform as Transformation?)?.name,
                                otherOptions = Options(
                                    mapOf(
                                        Option.Scale.LOW to lowColor,
                                        Option.Scale.HIGH to highColor,
                                        Option.Scale.SCALE_MAPPER_KIND to Option.Scale.MapperKind.COLOR_GRADIENT
                                    )
                                )
                            )

                        }

                        ALPHA -> scaleAlpha(
                            limits = (domainMin to domainMax).wrap(),
                            range = (rangeMin to rangeMax).wrap() as Pair<Number, Number>?, //todo
                            name = name,
                            breaks = breaks?.map { it as Number },
                            labels = labels,
                            guide = legendType,
                            trans = (transform as Transformation?)?.name,
                            format = format,
                            naValue = naValue as? Number
                        )
                        // TODO  SYMBOL -> TODO("cant apply contunuous scale")
                        else -> TODO()
                    }
                }

                is CustomScale -> when (this) {
                    is ScaleColorGrey<*> -> when (aes) {
                        COLOR -> scaleColorGrey(
                            paletteRange?.first,
                            paletteRange?.second,
                            name = name,
                            breaks = breaks?.map { it as Number }, // todo
                            labels = labels,
                            guide = legendType,
                            limits = domainLimits.wrap(),
                            trans = transform?.name,
                            format = format,
                            naValue = naValue
                        )

                        FILL -> scaleFillGrey(
                            paletteRange?.first,
                            paletteRange?.second,
                            name = name,
                            breaks = breaks?.map { it as Number }, // todo
                            labels = labels,
                            guide = legendType,
                            limits = domainLimits.wrap(),
                            trans = transform?.name,
                            format = format,
                            naValue = naValue
                        )

                        else -> TODO()
                    }

                    is ScaleColorHue<*> -> when (aes) {
                        COLOR -> scaleColorHue(
                            huesRange,
                            chroma,
                            luminance,
                            hueStart,
                            direction?.value,
                            name = name,
                            breaks = breaks?.map { it as Number }, // todo
                            labels = labels,
                            guide = legendType,
                            limits = domainLimits.wrap(),
                            trans = transform?.name,
                            format = format,
                            naValue = naValue
                        )

                        FILL -> scaleFillHue(
                            huesRange,
                            chroma,
                            luminance,
                            hueStart,
                            direction?.value,
                            name = name,
                            breaks = breaks?.map { it as Number }, // todo
                            labels = labels,
                            guide = legendType,
                            limits = domainLimits.wrap(),
                            trans = transform?.name,
                            format = format,
                            naValue = naValue
                        )

                        else -> TODO()
                    }

                    is ScaleColorBrewer<*> -> when (aes) {
                        COLOR -> scaleColorBrewer(
                            //type = type?.type,
                            type = null,
                            palette = palette?.name,
                            name = name,
                            breaks = breaks?.map { it as Number }, // todo
                            labels = labels,
                            guide = legendType,
                            limits = limits,
                            trans = transform?.name,
                            format = format,
                            naValue = naValue
                        )

                        FILL -> scaleFillBrewer(
                            //type = type?.type,
                            type = null,
                            palette = palette?.name,
                            name = name,
                            breaks = breaks?.map { it as Number }, // todo
                            labels = labels,
                            guide = legendType,
                            limits = limits,
                            trans = transform?.name,
                            format = format,
                            naValue = naValue
                        )

                        else -> TODO()
                    }

                    is ScaleContinuousColorGradient2<*> -> when (aes) {
                        COLOR -> scaleColorGradient2(
                            low.wrap(),
                            mid.wrap(),
                            high.wrap(),
                            midpoint,
                            name = name,
                            breaks = breaks?.map { it as Number }, // todo
                            labels = labels,
                            guide = legendType,
                            limits = domainLimits.wrap(),
                            trans = transform?.name,
                            format = format,
                            naValue = naValue
                        )

                        FILL -> scaleFillGradient2(
                            low.wrap(),
                            mid.wrap(),
                            high.wrap(),
                            midpoint,
                            name = name,
                            breaks = breaks?.map { it as Number }, // todo
                            labels = labels,
                            guide = legendType,
                            limits = domainLimits.wrap(),
                            trans = transform?.name,
                            format = format,
                            naValue = naValue
                        )

                        else -> TODO()
                    }

                    is ScaleContinuousColorGradientN<*> -> when (aes) {
                        COLOR -> scaleColorGradientN(
                            rangeColors.map { it.wrap() },
                            name = name,
                            breaks = breaks?.map { it as Number }, // todo
                            labels = labels,
                            guide = legendType,
                            limits = domainLimits.wrap(),
                            trans = transform?.name,
                            format = format,
                            naValue = naValue
                        )

                        FILL -> scaleFillGradientN(
                            rangeColors.map { it.wrap() },
                            name = name,
                            breaks = breaks?.map { it as Number }, // todo
                            labels = labels,
                            guide = legendType,
                            limits = domainLimits.wrap(),
                            trans = transform?.name,
                            format = format,
                            naValue = naValue
                        )

                        else -> TODO()
                    }

                    else -> TODO()
                }

                else -> TODO()
            }
        }

        else -> TODO("error")
    }

}

internal val categoricalTypes = listOf(typeOf<String>(), typeOf<Boolean>(), typeOf<Char>())

// todo
internal fun KType.isCategoricalType(): Boolean {
    return this in categoricalTypes
}
