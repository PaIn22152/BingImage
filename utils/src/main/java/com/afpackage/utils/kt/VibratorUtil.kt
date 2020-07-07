package com.afpackage.utils.kt

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

/**
 * Project    AppLock
 * Path       com.aftest.applock.utils
 * Date       2020/03/18 - 14:04
 * Author     Payne.
 * About      类描述：
 *
 * 手机振动
 *
need permissions
<uses-permission android:name="android.permission.VIBRATE" />
 */
class VibratorUtil {
    companion object {
        fun vibrate(context: Context, milliseconds: Long) {
            val vibrator: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

            /**
             * 创建一次性振动
             * @param milliseconds 震动时长（ms）
             * @param amplitude 振动强度。这必须是1到255之间的值，或者DEFAULT_AMPLITUDE
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val vibrationEffect: VibrationEffect =
                    VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE)
//                val timing = longArrayOf(1000L, 3000L,1000L,3000L,1000L)
//                val vibrationEffect: VibrationEffect = VibrationEffect.createWaveform(timing, -1)
                vibrator.vibrate(vibrationEffect)

            } else {
                vibrator.vibrate(milliseconds)
            }

        }
    }
}