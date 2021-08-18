package com.example.connectivity.util

import android.content.Context
import android.net.*
import android.os.Build

class NetworkUtilK {
    fun readDefaultNetworkState(context: Context) {
        val connectivityManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getSystemService(ConnectivityManager::class.java)
        } else {
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        }

        //Android 7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                //有一个新网络可用时，回调该方法。对于之前传递到此方法中的 Network，将不会触发此回调
                //如果使用 registerDefaultNetworkCallback 注册的回调，那么仅当有一个新的最佳网络可用时，会触发该回调
                override fun onAvailable(network: Network) {
                    LogUtil.INSTANCE.debug("onAvailable")

                }

                //网络即将丢失时回调
                override fun onLosing(network: Network, maxMsToLive: Int) {
                    LogUtil.INSTANCE.debug("onLosing")
                }

                //网络断开连接时，或者该网络事件不再触发此 Callback 时回调
                override fun onLost(network: Network) {
                    LogUtil.INSTANCE.debug("onLost")
                }

                //ConnectivityManager.requestNetwork(android.net.NetworkRequest, android.net.ConnectivityManager.NetworkCallback, int) 中指定的超时时间内没有找到可用网络，或者 requestNetwork(...) 无法被满足时回调。（即没有可用网络时回调）
                override fun onUnavailable() {
                    LogUtil.INSTANCE.debug("onUnavailable")
                }

                //网络的 capability（功能） 发生变更时调用
                override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                    LogUtil.INSTANCE.debug("onCapabilitiesChanged")
                }

                //网络的 LinkProperty 发生变更时回调
                override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
                    LogUtil.INSTANCE.debug("onLinkPropertiesChanged")
                }

                //网络的阻塞状态发生变更时回调（网络变为阻塞或者变为非阻塞）
                override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
                    LogUtil.INSTANCE.debug("onBlockedStatusChanged")                }
            })
        }
    }

    fun readAllNetworkState(context: Context) {
        val connectivityManager = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            context.getSystemService(ConnectivityManager::class.java)
        } else {
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        }

        // > Android 5.0
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

            //监听所有未计量网络的状态的更改，NetworkRequest 用于配置网络的过滤规则，可以监听指定 Capability 和 Transport 的网络的状态的变更
            connectivityManager.registerNetworkCallback(networkRequest, object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                }

                override fun onLosing(network: Network, maxMsToLive: Int) {
                }

                override fun onLost(network: Network) {
                }

                override fun onUnavailable() {
                }

                override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                }

                override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
                }

                override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
                }
            })
        }
    }
}