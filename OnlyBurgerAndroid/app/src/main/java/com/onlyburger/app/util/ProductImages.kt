package com.onlyburger.app.util

import androidx.annotation.DrawableRes
import com.onlyburger.app.R

/**
 * Maps a product id to its bundled drawable (res/drawable/product_<id>.jpg), mirroring
 * the web app's convention of naming images by product id. Returns null when there is no
 * image for that id, in which case the UI shows a named placeholder instead.
 *
 * To add or change an image: drop product_<id>.jpg into res/drawable and add a branch here.
 */
object ProductImages {
    @DrawableRes
    fun resFor(productId: Int): Int? = when (productId) {
        1 -> R.drawable.product_1
        2 -> R.drawable.product_2
        3 -> R.drawable.product_3
        4 -> R.drawable.product_4
        5 -> R.drawable.product_5
        6 -> R.drawable.product_6
        7 -> R.drawable.product_7
        8 -> R.drawable.product_8
        9 -> R.drawable.product_9
        10 -> R.drawable.product_10
        11 -> R.drawable.product_11
        12 -> R.drawable.product_12
        13 -> R.drawable.product_13
        14 -> R.drawable.product_14
        else -> null
    }
}
