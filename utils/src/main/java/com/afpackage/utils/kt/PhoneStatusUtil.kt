package com.afpackage.utils.kt

import android.app.ActivityManager
import android.app.KeyguardManager
import android.content.Context
import android.content.Context.KEYGUARD_SERVICE
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.PowerManager
import android.net.NetworkInfo
import android.net.ConnectivityManager
import android.os.Build


/**
 * Project    AppLock
 * Path       com.afpackage.utils.kt
 * Date       2020/03/17 - 14:40
 * Author     Payne.
 * About      类描述：
 * 手机状态帮助类
 */

/**
 * 手机是否亮屏状态
 * */
fun isScreenLight(context: Context): Boolean {
    val keyguardManager = context.getSystemService(KEYGUARD_SERVICE) as KeyguardManager
    return if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT_WATCH) {
            (context.getSystemService(POWER_SERVICE) as PowerManager).isInteractive && !keyguardManager.isKeyguardLocked
        } else {
            (context.getSystemService(POWER_SERVICE) as PowerManager).isScreenOn && !keyguardManager.isKeyguardLocked
        }
    } else true
}


var netCallback = false//是否有NetworkCallback回调
var netUsable = false//通过回调拿到的状态，网络是否可用

/**
 * 手机网络是否可用
 * need permissions
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * */
fun isNetUsable(context: Context): Boolean {
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP && netCallback) {
        return netUsable
    }
    val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkinfo = manager.activeNetworkInfo
    return !(networkinfo == null || !networkinfo.isAvailable)

}


/**
 * 判断当前界面是否是桌面
 */
fun isHome(context: Context, pkg: String): Boolean {
    val homes = getHomes(context)
    for (s in homes) {
        if (pkg == s) {
            return true
        }
    }
    return false
}

/**
 * com.google.android.googlequicksearchbox
 *
 * 获得属于桌面的应用的应用包名称
 * @return 返回包含所有包名的字符串列表
 */
fun getHomes(context: Context): List<String> {

    val names = ArrayList<String>()
    val packageManager = context.packageManager
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_HOME)

    // resolveActivity 返回正在使用的桌面应用
    val resolveInfo =
        packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)

    if (resolveInfo != null) {
        names.add(resolveInfo.activityInfo.packageName)
    }
    return names

}







