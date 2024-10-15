@file: JvmName("DeviceDetail")
package sword.devicedetail

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import sword.CommandExecutor
import sword.io.JavaFileIO
import sword.logger.SwordLog
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Locale

private const val tag = "DeviceDetail"

/**
 * 获取设备 CPU 架构信息
 */
fun getCpuAbi(): String {
  val abi = BufferedReader(
    InputStreamReader(
      Runtime.getRuntime().exec("getprop ro.product.cpu.abi").inputStream
    )
  ).readLine()
  SwordLog.debug(tag, "getCpuAbi, abi string: $abi")
  return if (abi.contains("x86")) {
    "x86"
  } else if (abi.contains("armeabi-v7a")) {
    "armeabi-v7a"
  } else if (abi.contains("arm64-v8a")) {
    "arm64-v8a"
  } else {
    "armeabi"
  }
}

fun getCpuModel(): String? {
  val cpuInfo = getCpuInfo()
  return if (TextUtils.isEmpty(cpuInfo["Hardware"])) {
    "${cpuInfo["ro.soc.manufacturer"]} ${cpuInfo["ro.soc.model"]}"
  } else {
    cpuInfo["Hardware"]
  }
}

//todo：
fun getCpuInfo(): Map<String, String> {
  val result = mutableMapOf<String, String>()
  
  JavaFileIO.readFile("/proc/cpuinfo") { line ->
    SwordLog.debug(tag, "read cpuinfo: $line")
    if (line.contains(":")) {
      line.split(":").let { 
        result[it[0].trim()] = it[1]
        SwordLog.debug("${it[0]} = ${it[1]}")
      }
    }
  }
  
  val propertiesList = listOf("ro.soc.manufacturer", "ro.soc.model")
  if (TextUtils.isEmpty(result["Hardware"])) {
    result.putAll(getSystemProperties(propertiesList))
  }
  return result
}


//todo: 统计耗时
@SuppressLint("DiscouragedPrivateApi")
private fun getSystemProperties(propNames: List<String>): Map<String, String> {
  SwordLog.info(tag, "----------------- getSystemProperties ---------------")
  val startTime = System.currentTimeMillis()
  val result = mutableMapOf<String, String>()
  val getStringMethod = Build::class.java.getDeclaredMethod("getString", String::class.java)
  getStringMethod.isAccessible = true
  propNames.forEach { propName ->
    val value = getStringMethod.invoke(null, propName) as String
    SwordLog.debug(tag, "$propName=$value")
    result[propName] = value
  }
  val costTime = System.currentTimeMillis() - startTime
  SwordLog.info(tag, "----------------- end getSystemProperties time: $costTime ---------------")
  return result
}

//todo：实现多个 getprop 命令放在一个 Process 中执行，目前是一个 getprop 命令
private fun getSystemPropertiesByShell(propNames: List<String>): Map<String, String> {
  SwordLog.info(tag, "----------------- getSystemPropertiesByShell ---------------")
  val startTime = System.currentTimeMillis()
  val results = mutableMapOf<String, String>()
  propNames.forEach { propName -> 
    val propValue = CommandExecutor.executeCommand("getprop $propName")
    results[propName] = propValue
    SwordLog.info(tag, "$propName=$propValue")
  }
  val costTime = System.currentTimeMillis() - startTime
  SwordLog.info(tag, "----------------- end getSystemPropertiesByShell time: $costTime ---------------")
  return results
}


/**
 * 打印设备所在地区信息
 */
fun getLocal(): String {
  val locale = Locale.getDefault()
  return "language: ${locale.language}, Country: ${locale.country}, languageTag: ${locale.toLanguageTag()}, displayCountry: ${locale.displayCountry}, getDisplayLanguage: ${locale.displayLanguage}, getDisplayName: ${locale.displayName}, getDisplayScript: ${locale.displayScript}, getDisplayVariant: ${locale.displayVariant}, getISO3Country: ${locale.isO3Country}, getISO3Language: ${locale.isO3Language}, getScript: ${locale.script}, getVariant: ${locale.variant}"
}


/**
 * 获取设备 IP 地址
 */
fun getIpAddress() {

  /*
    1. 通过 NetworkInterface 获取 IP 地址，可能会获取不到
    2. 通过 TelephonyManager 获取类似 MCC 或者 SIM 卡运营商所在地区的国家/地区代码
    3. 通过 WifiManager 获取 IP 地址
    4. 由服务器端下发 IP 地址，通过 IpSeekUtils 获取 IP 地址所在地区/国家
    5. 访问外部连接
          http://pv.sohu.com/cityjson
          http://pv.sohu.com/cityjson?ie=utf-8
          http://ip.chinaz.com/getip.aspx
    */
  SwordLog.debug(tag, "shell 获取外网 ip：${CommandExecutor.execShellCommand(false, "curl ifconfig.me").successString}")
}

fun getAndroidId(context: Context): String {
  return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}

fun getBuildInfo(): String {
  return "Brand" + Build.BRAND + "/" +
          Build.PRODUCT + "/" +
          Build.DEVICE + "/" +
          Build.ID + "/"
}