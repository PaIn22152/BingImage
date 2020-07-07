package com.afpackage.utils.kt

import android.annotation.TargetApi
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build

/**
 * Project    AppLock
 * Path       com.afpackage.utils.kt
 * Date       2020/03/19 - 11:04
 * Author     Payne.
 * About      类描述：
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class NetworkCallbackImpl : ConnectivityManager.NetworkCallback() {

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        logD("onAvailable: 网络已连接")
        netCallback = true
        netUsable = true
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        logD("onLost: 网络已断开")
        netCallback = true
        netUsable = false
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    logD("onCapabilitiesChanged: 网络类型为wifi")
                }
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    logD("onCapabilitiesChanged: 蜂窝网络")
                }
                else -> {
                    logD("onCapabilitiesChanged: 其他网络")
                }
            }
        }
    }

}

