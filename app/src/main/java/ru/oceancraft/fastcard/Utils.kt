package ru.oceancraft.fastcard

import android.graphics.Color
import kotlin.math.sqrt

fun colorWillBeMasked(color: Int): Boolean {
    val rgb = intArrayOf(Color.red(color), Color.green(color), Color.blue(color))
    val brightness = sqrt(
        rgb[0] * rgb[0] * .241 + rgb[1] * rgb[1] * .691 + rgb[2] * rgb[2] * .068
    ).toInt()
    if (brightness <= 40) {
        return false
    } else if (brightness >= 215) {
        return true
    }
    return false
}