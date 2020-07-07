package com.afpackage.utils.kt.base

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import com.afpackage.utils.kt.*
import io.reactivex.disposables.Disposable


/**
 * Project    AppLock
 * Path       com.afpackage.utils.kt.base
 * Date       2020/03/16 - 11:33
 * Author     Payne.
 * About      类描述：动态申请权限
 */
abstract class RequestPermissionActivity : BaseActivity() {

    companion object {
        private const val MANAGE_OVERLAY_REQ = 1005//悬浮窗权限
        private const val NOTIFICATION_LISTENER_REQ = 1006//通知使用权
        private const val USAGE_ACCESS_REQ = 1007//查看应用使用情况
        private const val ACCESSIBILITY_REQ = 1008//无障碍 辅助功能权限


        //android.Manifest.permission.下 的权限申请
        private const val NORMAL_MANIFEST = 1009//普通manifest权限

        const val TO_APP_SETTING = 2001//跳转到应用设置界面
    }


    private val callbackMap: HashMap<Int, OnPermissionRequestCallback> = HashMap()
    var rxInterval: Disposable? = null


    interface OnPermissionRequestCallback { //权限申请回调
        fun onGrant() //授权
        fun onDeny() //拒绝
        fun onDeniedAndNotAsk()//拒绝且不再提示
    }

    override fun onDestroy() {
        super.onDestroy()

        rxInterval?.dispose()
    }


    protected fun requestPermission4ManageOverlay(callback: OnPermissionRequestCallback) {
        callbackMap[MANAGE_OVERLAY_REQ] = callback

        rxInterval?.dispose()
        isStartActivity2RequestPermission = false

        if (!permissionCheck_ManageOverlay(this)) {
            val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            } else {
                Intent(ACTION_MANAGE_OVERLAY_PERMISSION)
            }
            startActivityForResult(intent, MANAGE_OVERLAY_REQ)

            isStartActivity2RequestPermission = true

            rxInterval = rxInterval(300, true, object : RXCallback {
                override fun call() {
                    if (permissionCheck_ManageOverlay(this@RequestPermissionActivity)) {
                        finishActivity(MANAGE_OVERLAY_REQ)
                        rxInterval?.dispose()
                    }
                }

            })
        } else {
            callback.onGrant()
        }
    }

    protected fun requestPermission4NotificationListener(callback: OnPermissionRequestCallback) {
        callbackMap[NOTIFICATION_LISTENER_REQ] = callback

        rxInterval?.dispose()
        isStartActivity2RequestPermission = false

        if (!permissionCheck_NotificationListener(this)) {
            val intent: Intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            } else {
                Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS)
            }
            startActivityForResult(intent, NOTIFICATION_LISTENER_REQ)


//            rxDelay(1000L, true, object : RXCallback {
//                override fun call() {
//                    startActivity<GuideActivity>()
//                }
//            })
            isStartActivity2RequestPermission = true

            rxInterval = rxInterval(300, true, object : RXCallback {
                override fun call() {
                    if (permissionCheck_NotificationListener(this@RequestPermissionActivity)) {
                        finishActivity(NOTIFICATION_LISTENER_REQ)
                        rxInterval?.dispose()
                    }
                }

            })
        } else {
            callback.onGrant()
        }
    }


    protected fun requestPermission4Accessibility(callback: OnPermissionRequestCallback) {//无障碍 辅助功能权限
        callbackMap[ACCESSIBILITY_REQ] = callback

        rxInterval?.dispose()
        isStartActivity2RequestPermission = false

        if (!permissionCheck_Accessibility(this)) {

            // 引导至辅助功能设置页面
            startActivityForResult(
                Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS),
                ACCESSIBILITY_REQ
            )
            isStartActivity2RequestPermission = true

            rxInterval = rxInterval(300, true, object : RXCallback {
                override fun call() {
                    if (permissionCheck_Accessibility(this@RequestPermissionActivity)) {
                        finishActivity(ACCESSIBILITY_REQ)
                        rxInterval?.dispose()
                    }
                }
            })
        } else {
            callback.onGrant()
        }
    }




    var isStartActivity2RequestPermission = false

    protected fun requestPermission4UsageAccess(callback: OnPermissionRequestCallback) {
        logD("requestPermission4UsageAccess")

        rxInterval?.dispose()
        isStartActivity2RequestPermission = false

        callbackMap[USAGE_ACCESS_REQ] = callback

        if (!permissionCheck_UsageAccess(this)) {
            val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            } else {
                Intent(ACTION_USAGE_ACCESS_SETTINGS)
            }
            startActivityForResult(intent, USAGE_ACCESS_REQ)


            isStartActivity2RequestPermission = true


            rxInterval = rxInterval(300, true, object : RXCallback {
                override fun call() {
                    if (permissionCheck_UsageAccess(this@RequestPermissionActivity)) {
                        finishActivity(USAGE_ACCESS_REQ)
                        rxInterval?.dispose()
                    }
                }

            })


        } else {
            callback.onGrant()
        }
    }

//    protected fun requestPermission4ReadWriteStorage(callback: OnPermissionRequestCallback) {
//        callbackMap[READ_WRITE_STORAGE] = callback
//        logD("")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!hasPermissionNormalManifest(this, WRITE_EXTERNAL_STORAGE)) {
//                val perm = arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
//                requestPermissions(perm, READ_WRITE_STORAGE)
//            }
//        } else {
//            callback.onGrant()
//        }
//    }


    protected fun requestPermission4NormalManifest(
        perm: String,
        callback: OnPermissionRequestCallback
    ) {
        callbackMap[NORMAL_MANIFEST] = callback
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermissionNormalManifest(this, perm)) {
                val perms = arrayOf(perm)
                requestPermissions(perms, NORMAL_MANIFEST)
            } else {
                callback.onGrant()
            }
        } else {
            callback.onGrant()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var allGrant = true
        val callback = callbackMap[requestCode] ?: return
        when (requestCode) {
            NORMAL_MANIFEST -> {
                for ((num, grantRes) in grantResults.withIndex()) {
                    if (grantRes == PackageManager.PERMISSION_DENIED) {
                        allGrant = false
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                            && !shouldShowRequestPermissionRationale(permissions[num])
                        ) {
                            callback.onDeniedAndNotAsk()
                            return
                        }
                        break
                    }
                }
                if (allGrant) {
                    callback.onGrant()
                } else {
                    callback.onDeny()
                }
            }
            else -> {

            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val callback = callbackMap[requestCode] ?: return
        when (requestCode) {
            USAGE_ACCESS_REQ -> {
                if (!permissionCheck_UsageAccess(this)) {
                    callback.onDeny()
                } else {
                    callback.onGrant()
                }
            }
            NOTIFICATION_LISTENER_REQ -> {
                if (!permissionCheck_NotificationListener(this)) {
                    callback.onDeny()
                } else {
                    callback.onGrant()
                }
            }
            MANAGE_OVERLAY_REQ -> {
                if (!permissionCheck_ManageOverlay(this)) {
                    callback.onDeny()
                } else {
                    callback.onGrant()
                }
            }
            ACCESSIBILITY_REQ -> {
                if (!permissionCheck_Accessibility(this)) {
                    callback.onDeny()
                } else {
                    callback.onGrant()
                }
            }
            else -> {

            }

        }

    }


}

fun hasPermissionNormalManifest(context: Context, perm: String): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val checkSelfPermission = context.checkSelfPermission(perm)
        return PackageManager.PERMISSION_GRANTED == checkSelfPermission
    }
    return true
}


