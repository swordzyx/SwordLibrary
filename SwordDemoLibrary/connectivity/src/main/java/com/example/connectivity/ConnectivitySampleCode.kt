package com.example.connectivity

import android.annotation.SuppressLint
import android.content.Context
import android.net.*
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.sword.LogUtil
import kotlinx.coroutines.launch
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 检查网络是否连通 API < 23
 * 需要 android.permission.ACCESS_NETWORK_STATE 权限
 */
fun netConnectedApiLess23(context: Context): Boolean {
  val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  var wifiConnected = false
  var mobildConnected = false
  connectivityManager.allNetworkInfo.forEach { networkInfo -> 
    if (networkInfo.type == ConnectivityManager.TYPE_WIFI) {
      wifiConnected = networkInfo.isConnected
    }
    if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
      mobildConnected = networkInfo.isConnected
    }
  }
  return wifiConnected || mobildConnected
}

/**
 * 检查网络是否连通 23 <= API < 31
 * 需要 android.permission.ACCESS_NETWORK_STATE 权限
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@SuppressLint("MissingPermission")
fun netConnectedApiLess31(context: Context): Boolean {
  val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  var wifiConnected = false
  var mobileConnected = false
  connectivityManager.allNetworks.forEach { network ->
    connectivityManager.getNetworkInfo(network)?.apply {
      if (type == ConnectivityManager.TYPE_WIFI) {
        wifiConnected = isConnected
      }
      if (type == ConnectivityManager.TYPE_MOBILE) {
        mobileConnected = isConnected
      }
    }   
  }
  return wifiConnected || mobileConnected
}

/**
 * 检查网络是否连通，API >= 31
 */
@SuppressLint("MissingPermission")
@RequiresApi(Build.VERSION_CODES.N)
fun netConnectedApiGreater31(context: Context) {
  val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network) {
      LogUtil.debug("network available, linkProperties: ${connectivityManager.getLinkProperties(network)}; capabilities: ${connectivityManager.getNetworkCapabilities(network)}")
      super.onAvailable(network)
    }

    override fun onLosing(network: Network, maxMsToLive: Int) {
      LogUtil.debug("network losing, linkProperties: ${connectivityManager.getLinkProperties(network)}; capabilities: ${connectivityManager.getNetworkCapabilities(network)}; maxMsToLive: $maxMsToLive")
      super.onLosing(network, maxMsToLive)
    }

    override fun onLost(network: Network) {
      LogUtil.debug("network lost, linkProperties: ${connectivityManager.getLinkProperties(network)}; capabilities: ${connectivityManager.getNetworkCapabilities(network)}")
      super.onLost(network)
    }

    override fun onUnavailable() {
      LogUtil.debug("network unAvailable")
      super.onUnavailable()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
      LogUtil.debug("network capabilitiesChanged, linkProperties: ${connectivityManager.getLinkProperties(network)}; capabilities: ${connectivityManager.getNetworkCapabilities(network)}")
      if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        LogUtil.debug(" new capabilities, ownerUID: ${networkCapabilities.ownerUid}; capabilities: ${networkCapabilities.capabilities}")
      } else {
        LogUtil.debug(" new capabilities, ownerUID: ${networkCapabilities.ownerUid}")
      }
      super.onCapabilitiesChanged(network, networkCapabilities)
    }

    override fun onLinkPropertiesChanged(network: Network, linkProperties: LinkProperties) {
      LogUtil.debug("network linkProperties changed, network linkProperties: ${connectivityManager.getLinkProperties(network)}; capabilities: ${connectivityManager.getNetworkCapabilities(network)}")
      LogUtil.debug("new link property, domains: ${linkProperties.domains}, httpProxy: ${linkProperties.httpProxy}, interfaceName: ${linkProperties.interfaceName}")
      super.onLinkPropertiesChanged(network, linkProperties)
    }

    override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
      LogUtil.debug("network capabilitiesChanged, linkProperties: ${connectivityManager.getLinkProperties(network)}; capabilities: ${connectivityManager.getNetworkCapabilities(network)}")
      super.onBlockedStatusChanged(network, blocked)
    }
  })
}




/*--------------------------------Android Developer - Connectivity - Connect to the network---------------------------------------*/ 
data class User(val name: String) 

/**
 * 通过 retrofit 发送网络请求。Service 只用于执行网络请求
 * */
interface UserService {
  @GET("/users/{id}") 
  suspend fun getUser(@Path("id") id: String): User
}

class UserRepository constructor(private val userService: UserService) {
  suspend fun getUserById(id: String): User {
    return userService.getUser(id)
  }
}

class MainViewModel constructor(savedStateHandle: SavedStateHandle, userRepository: UserRepository): ViewModel() {
  private val userId: String = savedStateHandle["uid"] ?: throw IllegalAccessException("Missing user ID")
  
  //value 可变的 LiveData 对象，_user 中的值发生改变时，所有使用了 _user 的 View 内容也会自动刷新。
  private val _user = MutableLiveData<User>()
  //viewModelScope 是 CoroutineScope 类型实例，CoroutineScope 是对 CoroutineContext 的封装。
  init {
    //viewModelScope 的使用需要引入 "androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0" 依赖
    viewModelScope.launch { 
      val user = userRepository.getUserById(userId)
      _user.value = user
    }
  }
}

