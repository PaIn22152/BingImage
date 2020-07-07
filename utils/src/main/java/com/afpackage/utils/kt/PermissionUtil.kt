package com.afpackage.utils.kt

import android.app.usage.UsageStatsManager
import android.content.ComponentName
import android.content.Context
import android.os.Binder
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.provider.Settings
import android.provider.Settings.Secure
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils

//import androidx.core.content.ContextCompat


/**
 * Project    AppLock
 * Path       com.afpackage.utils.kt
 * Date       2020/03/16 - 11:33
 * Author     Payne.
 * About      类描述：
 */


// 此方法用来判断当前应用的辅助功能服务是否开启
fun permissionCheck_Accessibility(context: Context): Boolean {
    var accessibilityEnabled = 0
    try {
        accessibilityEnabled = Secure.getInt(
            context.contentResolver,
            Secure.ACCESSIBILITY_ENABLED
        )
    } catch (e: SettingNotFoundException) {
    }
    if (accessibilityEnabled == 1) {
        val services = Secure.getString(
            context.contentResolver,
            Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        if (services != null) {
            return services.toLowerCase().contains(context.packageName.toLowerCase())
        }
    }
    return false
}

//权限检查，ACTION_USAGE_ACCESS_SETTINGS，有权查看使用情况
fun permissionCheck_UsageAccess(context: Context): Boolean {
//    logD("permissionCheck_UsageAccess")
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP_MR1) {
        val ts = System.currentTimeMillis()
        val usageStatsManager: UsageStatsManager?

        usageStatsManager = context
            .getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val queryUsageStats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_BEST, 0, ts
        )
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return false
        }
    }
    return true
}


//权限检查，ACTION_NOTIFICATION_LISTENER_SETTINGS，通知使用权
fun permissionCheck_NotificationListener(context: Context): Boolean {
    logD(" permissionCheck_NotificationListener ")
    val pkgName = context.packageName
    val flat = Secure.getString(
        context.contentResolver,
        "enabled_notification_listeners"
    )
    if (!TextUtils.isEmpty(flat)) {
        val names = flat.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (name in names) {
            val cn = ComponentName.unflattenFromString(name)
            if (cn != null) {
                if (TextUtils.equals(pkgName, cn.packageName)) {
                    return true
                }
            }
        }
    }
    return false
}



//权限检查，ACTION_MANAGE_OVERLAY_PERMISSION，悬浮窗权限
fun permissionCheck_ManageOverlay(context: Context): Boolean {
    if (VERSION.SDK_INT < VERSION_CODES.KITKAT)
        return true
    return if (VERSION.SDK_INT < VERSION_CODES.M) {
        try {
            var cls = Class.forName("android.content.Context")
            val declaredField = cls.getDeclaredField("APP_OPS_SERVICE")
            declaredField.isAccessible = true
            var obj = declaredField.get(cls)
            if (obj !is String) {
                return false
            }
            obj = cls.getMethod("getSystemService", String::class.java).invoke(context, obj)
            cls = Class.forName("android.app.AppOpsManager")
            val declaredField2 = cls.getDeclaredField("MODE_ALLOWED")
            declaredField2.isAccessible = true
            val checkOp = cls.getMethod("checkOp", Integer.TYPE, Integer.TYPE, String::class.java)
            val result = checkOp.invoke(obj, 24, Binder.getCallingUid(), context.packageName) as Int
            result == declaredField2.getInt(cls)
        } catch (e: Exception) {
            false
        }

    } else {
        Settings.canDrawOverlays(context)
    }
}







