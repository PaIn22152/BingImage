package com.afpackage.utils.kt

import android.app.Activity
import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.content.res.Resources


/**
 * Project    AppLock
 * Path       com.afpackage.utils.kt
 * Date       2020/03/17 - 15:59
 * Author     Payne.
 * About      类描述：
 * 今日头条屏幕适配方案
 */
// 系统的Density
private var sNoncompatDensity: Float = 0.toFloat()
// 系统的ScaledDensity
private var sNoncompatScaledDensity: Float = 0.toFloat()


fun adaptScreen(activity: Activity) {
    val application = activity.application
    val displayMetrics = application.resources.displayMetrics
    if (sNoncompatDensity == 0f) {
        sNoncompatDensity = displayMetrics.density
        sNoncompatScaledDensity = displayMetrics.scaledDensity
        // 监听在系统设置中切换字体
        application.registerComponentCallbacks(object : ComponentCallbacks {
            override fun onConfigurationChanged(newConfig: Configuration?) {
                if (newConfig != null && newConfig.fontScale > 0) {
                    sNoncompatScaledDensity =
                        application.resources.displayMetrics.scaledDensity
                }
            }

            override fun onLowMemory() {

            }
        })
    }

    // 此处以400dp的设计图作为例子
//            val targetDensity = displayMetrics.widthPixels.toFloat() / 400f
//            val targetDensity = displayMetrics.widthPixels.toFloat() / 300f
    val targetDensity = displayMetrics.widthPixels.toFloat() / 360f
    val targetScaledDensity = targetDensity * (sNoncompatScaledDensity / sNoncompatDensity)
    val targetDensityDpi = (160 * targetDensity).toInt()
    displayMetrics.density = targetDensity
    displayMetrics.scaledDensity = targetScaledDensity
    displayMetrics.densityDpi = targetDensityDpi

    val activityDisplayMetrics = activity.resources.displayMetrics
    activityDisplayMetrics.density = targetDensity
    activityDisplayMetrics.scaledDensity = targetScaledDensity
    activityDisplayMetrics.densityDpi = targetDensityDpi


    val systemDisplayMetrics = Resources.getSystem().displayMetrics
    systemDisplayMetrics.density = targetDensity
    systemDisplayMetrics.scaledDensity = targetScaledDensity
    systemDisplayMetrics.densityDpi = targetDensityDpi

}