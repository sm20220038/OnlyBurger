package com.onlyburger.app.util

import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToLong

/**
 * Formats prices as Serbian dinar, matching the web app: whole numbers with a
 * thousands separator and an "RSD" suffix, e.g. 1060.0 -> "1.060 RSD".
 */
object Money {
    private val formatter: NumberFormat =
        NumberFormat.getIntegerInstance(Locale("sr", "RS"))

    fun format(value: Double): String = "${formatter.format(value.roundToLong())} RSD"
}
