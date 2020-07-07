package com.afpackage.utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Build
import com.afpackage.utils.kt.NetworkCallbackImpl

/**
 * Project    AppLock
 * Path       com.afpackage.utils
 * Date       2020/03/19 - 11:07
 * Author     Payne.
 * About      类描述：
 * BaseApplication类，一些需要初始化的util类，在此初始化
 * 应用层application继承此类
 * 如果应用层的application不能继承此类，也可把初始化代码复制到应用层application
 */
abstract class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        init()

    }

    fun init() {

        //api21之后，使用NetworkCallback方法获得手机网络状态
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val networkCallback = NetworkCallbackImpl()
            val builder = NetworkRequest.Builder()
            val request = builder.build()
            val connMgr =
                this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connMgr.registerNetworkCallback(request, networkCallback)
        }
    }
}