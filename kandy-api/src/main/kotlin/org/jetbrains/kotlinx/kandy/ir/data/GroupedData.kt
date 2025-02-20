/*
* Copyright 2020-2023 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
*/

package org.jetbrains.kotlinx.kandy.ir.data

import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.GroupBy
import org.jetbrains.kotlinx.dataframe.api.groupBy
import org.jetbrains.kotlinx.kandy.dsl.internal.concatFixed


/**
 * Represents data that has been grouped by splits rows of a [DataFrame] using one or more columns as grouping keys.
 *
 * @property groupBy a grouped dataframe where the initial columns as the grouping keys,
 * and the last column stores a dataframe as its cell entry.
 * @property keys the column names used for grouping the original dataframe.
 * @property dataFrame the initial dataframe prior to grouping.
 */
public data class GroupedData(
    public val dataFrame: DataFrame<*>,
    public val keys: List<String>
) : TableData {
    public val groupBy: GroupBy<*, *>
        get() = dataFrame.groupBy(*keys.toTypedArray())

    /**
     * Initializes a [GroupedData] instance by grouping the rows of the provided [NamedData] using the specified keys.
     *
     * @param origin the original [NamedData] to be grouped.
     * @param keys the column names are used as grouping keys.
     */
    public constructor(origin: NamedData, keys: List<String>) :
            this(origin.dataFrame, keys)

    /**
     * Initializes a [GroupedData] instance by grouping the rows of the provided [DataFrame] using the specified keys.
     *
     * @param dataFrame the original [DataFrame] to be grouped.
     * @param keys the column names are used as grouping keys.
     */
    public constructor(groupBy: GroupBy<*, *>) :
            this(groupBy.concatFixed(), groupBy.keys.columnNames())
}
