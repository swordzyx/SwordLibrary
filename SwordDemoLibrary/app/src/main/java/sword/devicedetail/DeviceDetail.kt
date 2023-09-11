package sword.devicedetail

import sword.ShellAdbUtil
import sword.SwordLog
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

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
  SwordLog.debug(tag, "shell 获取外网 ip：${ShellAdbUtil.execShellCommand(false, "curl ifconfig.me").successString}")
}