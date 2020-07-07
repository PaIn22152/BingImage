package com.afpackage.utils.kt

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * Project    AppLock
 * Path       com.aftest.applock.utils
 * Date       2020/04/01 - 19:31
 * Author     Payne.
 * About      类描述：
 */
fun scaleXY(v: View, duration: Long, vararg float: Float) {
    val scalex = ObjectAnimator.ofFloat(v, "scaleX", *float)
    val scaley = ObjectAnimator.ofFloat(v, "scaleY", *float)
    val animatorSet = AnimatorSet()
    animatorSet.duration = duration
    animatorSet.play(scalex).with(scaley)
    animatorSet.start()
}

fun scaleXY_Linear(v: View, duration: Long, vararg float: Float) {
    val scalex = ObjectAnimator.ofFloat(v, "scaleX", *float)
    val scaley = ObjectAnimator.ofFloat(v, "scaleY", *float)
    val animatorSet = AnimatorSet()
    animatorSet.interpolator = LinearInterpolator()
    animatorSet.duration = duration
    animatorSet.play(scalex).with(scaley)
    animatorSet.start()
}

