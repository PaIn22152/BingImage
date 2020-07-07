package com.afpackage.utils.kt

import android.content.Context
import android.os.Build
import androidx.annotation.ColorRes

/**
 * Project    AppLock
 * Path       com.aftest.applock.utils
 * Date       2020/04/09 - 11:17
 * Author     Payne.
 * About      类描述：
 */
 fun getColor(context: Context, @ColorRes res: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        context.getColor(res)
    } else {
        context.resources.getColor(res)
    }
}

