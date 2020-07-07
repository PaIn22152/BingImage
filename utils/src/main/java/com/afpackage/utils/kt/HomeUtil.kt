package com.afpackage.utils.kt

import android.content.Context
import android.content.Intent

/**
 * Project    AppLock
 * Path       com.aftest.applock.utils
 * Date       2020/03/17 - 10:25
 * Author     Payne.
 * About      类描述：
 */

/**
 * 模拟点击home键
 * */
fun homeClick(context: Context) {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
//    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    intent.addCategory(Intent.CATEGORY_HOME)
    context.startActivity(intent)
}
