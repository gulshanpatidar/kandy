/*
* Copyright 2020-2023 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
*/

package org.jetbrains.kotlinx.kandy.echarts.features.label

import org.jetbrains.kotlinx.kandy.echarts.features.text.TextStyle
import org.jetbrains.kotlinx.kandy.echarts.layers.*
import org.jetbrains.kotlinx.kandy.echarts.layers.context.*

/**
 * Label options settings for [line][line].
 * If a property isn't set or set to null, a default value will be used.
 *
 * * [position][LabelContext.position] - label [position][LabelPosition].
 * Includes position, distance and rotation settings.
 * `LabelPosition.position` may not apply to some types of plots.
 * By default, position is `top`, distance is `5`, rotate isn't defined
 * * [formatter][LabelContext.formatter] - data label formatter, supports string template.
 *
 * *String template*
 *
 * Model variation includes:
 *
 * - `{a}` - layers name.
 * - `{b}` - the name of a data item.
 * - `{c}` - the value of a data item.
 * - `{@xxx}` - the value of a column named _`xxx`_, for example, `{@product}` refers the value of `product` column.
 * - `{@[n]}` - the value of a column at the index of `n`, for example, `{@[3]}` refers the value at column[3].
 *
 * * [textStyle][LabelContext.textStyle] - [text style][TextStyle] settings
 * * [border][LabelContext.border] - [border][LabelBorder] settings
 *
 * ```kotlin
 * plot {
 *  line {
 *      label {
 *          position = LabelPosition.top(distance = 5, rotate = 45)
 *          formatter = "{b}: {@score}"
 *          textStyle.color(Color.BLUE)
 *          border {
 *              color = Color.RED
 *              width = 1.5
 *          }
 *      }
 *  }
 * }
 * ```
 *
 * @see line
 * @see LineContext
 * @see LabelContext
 * @see LabelPosition
 */
public fun LineContext.label(block: LabelContext.() -> Unit) {
    val label = LabelContext().apply(block).toLabelFeature()
    if (label != null) layerFeatures[LabelFeature.FEATURE_NAME] = label
}

/**
 * Label options settings for [area][area].
 * If a property isn't set or set to null, a default value will be used.
 *
 * * [position][LabelContext.position] - label [position][LabelPosition].
 * Includes position, distance and rotation settings.
 * `LabelPosition.position` may not apply to some types of plots.
 * By default, position is `top`, distance is `5`, rotate isn't defined
 * * [formatter][LabelContext.formatter] - data label formatter, supports string template.
 *
 * *String template*
 *
 * Model variation includes:
 *
 * - `{a}` - layers name.
 * - `{b}` - the name of a data item.
 * - `{c}` - the value of a data item.
 * - `{@xxx}` - the value of a column named _`xxx`_, for example, `{@product}` refers the value of `product` column.
 * - `{@[n]}` - the value of a column at the index of `n`, for example, `{@[3]}` refers the value at column[3].
 *
 * * [textStyle][LabelContext.textStyle] - [text style][TextStyle] settings
 * * [border][LabelContext.border] - [border][LabelBorder] settings
 *
 * ```kotlin
 * plot {
 *  area {
 *      label {
 *          position = LabelPosition.top(distance = 5, rotate = 45)
 *          formatter = "{b}: {@score}"
 *          textStyle.color = Color.BLUE
 *          border {
 *              color = Color.RED
 *              width = 1.5
 *          }
 *      }
 *  }
 * }
 * ```
 *
 * @see area
 * @see AreaContext
 * @see LabelContext
 * @see LabelPosition
 */
public fun AreaContext.label(block: LabelContext.() -> Unit) {
    val label = LabelContext().apply(block).toLabelFeature()
    if (label != null) layerFeatures[LabelFeature.FEATURE_NAME] = label
}

/**
 * Label options settings for [bars][bars].
 * If a property isn't set or set to null, a default value will be used.
 *
 * * [position][LabelContext.position] - label [position][LabelPosition].
 * Includes position, distance and rotation settings.
 * `LabelPosition.position` may not apply to some types of plots.
 * By default, position is `top`, distance is `5`, rotate isn't defined
 * * [formatter][LabelContext.formatter] - data label formatter, supports string template.
 *
 * *String template*
 *
 * Model variation includes:
 *
 * - `{a}` - layers name.
 * - `{b}` - the name of a data item.
 * - `{c}` - the value of a data item.
 * - `{@xxx}` - the value of a column named _`xxx`_, for example, `{@product}` refers the value of `product` column.
 * - `{@[n]}` - the value of a column at the index of `n`, for example, `{@[3]}` refers the value at column[3].
 *
 * * [textStyle][LabelContext.textStyle] - [text style][TextStyle] settings
 * * [border][LabelContext.border] - [border][LabelBorder] settings
 *
 * ```kotlin
 * plot {
 *  bars {
 *      label {
 *          position = LabelPosition.fromPx(10.px to 10.px)
 *          formatter = "{b}: {@score}"
 *          textStyle.color = Color.BLUE
 *      }
 *  }
 * }
 * ```
 *
 * @see bars
 * @see BarContext
 * @see LabelContext
 * @see LabelPosition
 */
public fun BarContext.label(block: LabelContext.() -> Unit) {
    val label = LabelContext().apply(block).toLabelFeature()
    if (label != null) layerFeatures[LabelFeature.FEATURE_NAME] = label
}

/**
 * Label options settings for [points][points].
 * If a property isn't set or set to null, a default value will be used.
 *
 * * [position][LabelContext.position] - label [position][LabelPosition].
 * Includes position, distance and rotation settings.
 * `LabelPosition.position` may not apply to some types of plots.
 * By default, position is `top`, distance is `5`, rotate isn't defined
 * * [formatter][LabelContext.formatter] - data label formatter, supports string template.
 *
 * *String template*
 *
 * Model variation includes:
 *
 * - `{a}` - layers name.
 * - `{b}` - the name of a data item.
 * - `{c}` - the value of a data item.
 * - `{@xxx}` - the value of a column named _`xxx`_, for example, `{@product}` refers the value of `product` column.
 * - `{@[n]}` - the value of a column at the index of `n`, for example, `{@[3]}` refers the value at column[3].
 *
 * * [textStyle][LabelContext.textStyle] - [text style][TextStyle] settings
 * * [border][LabelContext.border] - [border][LabelBorder] settings
 *
 * ```kotlin
 * plot {
 *  points {
 *      label {
 *          position = LabelPosition.INSIDE
 *          textStyle.color = Color.BLACK
 *      }
 *  }
 * }
 * ```
 *
 * @see points
 * @see PointContext
 * @see LabelContext
 * @see LabelPosition
 */
public fun PointContext.label(block: LabelContext.() -> Unit) {
    val label = LabelContext().apply(block).toLabelFeature()
    if (label != null) layerFeatures[LabelFeature.FEATURE_NAME] = label
}

/**
 * Label options settings for [pie][pie].
 * If a property isn't set or set to null, a default value will be used.
 *
 * * [position][LabelContext.position] - label [position][LabelPosition].
 * Includes position, distance and rotation settings.
 * `LabelPosition.position` may not apply to some types of plots.
 * By default, position is `top`, distance is `5`, rotate isn't defined
 * * [formatter][LabelContext.formatter] - data label formatter, supports string template.
 *
 * *String template*
 *
 * Model variation includes:
 *
 * - `{a}` - layers name.
 * - `{b}` - the name of a data item.
 * - `{c}` - the value of a data item.
 * - `{@xxx}` - the value of a column named _`xxx`_, for example, `{@product}` refers the value of `product` column.
 * - `{@[n]}` - the value of a column at the index of `n`, for example, `{@[3]}` refers the value at column[3].
 *
 * * [textStyle][LabelContext.textStyle] - [text style][TextStyle] settings
 * * [border][LabelContext.border] - [border][LabelBorder] settings
 *
 * ```kotlin
 * plot {
 *  pie {
 *      label {
 *          position = LabelPosition.inside(distance = 10)
 *      }
 *  }
 * }
 * ```
 *
 * @see pie
 * @see PieContext
 * @see LabelContext
 * @see LabelPosition
 */
public fun PieContext.label(block: LabelContext.() -> Unit) {
    val label = LabelContext().apply(block).toLabelFeature()
    if (label != null) layerFeatures[LabelFeature.FEATURE_NAME] = label
}
