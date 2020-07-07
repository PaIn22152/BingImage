package com.afpackage.utils.kt

import android.content.Context
import android.content.res.Resources
import kotlin.math.roundToInt

/**
 * Project    AppLock
 * Path       com.afpackage.utils.kt
 * Date       2020/03/18 - 11:43
 * Author     Payne.
 * About      类描述：
 */
private val DENSITY = Resources.getSystem().displayMetrics.density

fun dp2Px(dp: Int): Int {
    return (dp * DENSITY).roundToInt()
}

/**
 * 将sp值转换为px值，保证文字大小不变
 *
 * @param spValue
 * @return
 */
fun sp2px(context: Context, spValue: Float): Int {
    val fontScale = context.resources.displayMetrics.scaledDensity
    return (spValue * fontScale + 0.5f).toInt()
}