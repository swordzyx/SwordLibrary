package com.example.utilclass.dongliu.apk.parser

import android.text.TextUtils
import com.example.utilclass.apk.parse.Property
import com.example.utilclass.dongliu.apk.parser.xlcw.*
import net.dongliu.apk.parser.ApkFile
import okhttp3.*
import java.util.*

private val tempDir = "tempDir/"
private const val platformConfigPath = "assets/platform_config.properties"
private const val versionBytePath = "assets/AssetBundles/Android/version.bytes"
private val androidManifestPath = "AndroidManifest.xml"

//Loading 图位置
private val loadingImagePath = "assets/First_Loading.png"

//闪屏图位置
private val splashImagePath = "res/drawable/unity_static_splash.png"

class ApkReader(apkPath: String) {
  private val apkFile: ApkFile = ApkFile(apkPath)
  private val paraMap = mutableMapOf<String, String>()

  private fun parseApk() {
    paraMap.putAll(readPlatformConfig())
    paraMap.putAll(readVersionBytes())
  }

  //读取 assets/platform_config.properties 文件
  private fun readPlatformConfig(): Map<String, String> {
    println("---------------- start read $platformConfigPath content ------------------")
    val stringContent = String(apkFile.getFileData(platformConfigPath) ?: return emptyMap())


    return mutableMapOf<String, String>().apply {
      stringContent.split("\n").filter {
        val l = it.trim()
        l != "" && !l.startsWith("#")
      }.forEach { line ->
        println(line)
        val lineWithoutSpace = line.trim()
        println("lineWithoutSpace: $lineWithoutSpace")
        val equalIndex = lineWithoutSpace.indexOf("=")
        val key = lineWithoutSpace.substring(0 until equalIndex)
        val value =
          if (equalIndex == lineWithoutSpace.length - 1) "" else lineWithoutSpace.substring(
            equalIndex + 1..lineWithoutSpace.length
          )
        put(key, value)
      }
      println("----------------------- end read $platformConfigPath --------------------------")
    }
  }

  //读取 assets/AssetBundles/Android/version.bytes 文件中的 com.example.utilclass.apk.parse.xlcw.VersionNum 属性值
  private fun readVersionBytes(): Map<String, String> {
    println("---------------- start read $versionBytePath content ------------------")
    val stringContent = String(apkFile.getFileData(versionBytePath) ?: return emptyMap()).apply {
      substring(1 until length)
    }

    println("$versionBytePath content: $stringContent")

    return mutableMapOf<String, String>().apply {
      stringContent
        .replace("\"", "")
        .split(",")
        .forEach {
          val propertyString = it.split(":")

          if (propertyString.size == 2)
            put(propertyString[0], propertyString[1])
        }

      println("---------------- end read $versionBytePath ------------------")
    }
  }

  //读取 apk 版本相关信息
  private fun readApkMetaInfo(): List<Property> {
    return listOf(
      Property(appName, apkFile.apkMeta.name),
      Property(packageName, apkFile.apkMeta.packageName),
      Property(versionName, apkFile.apkMeta.versionName),
      Property(versionCode, apkFile.apkMeta.versionCode.toString()),
      Property(minSdkVersion, apkFile.apkMeta.minSdkVersion),
      Property(targetSdkVersion, apkFile.apkMeta.targetSdkVersion)
    )
  }

  //获取 B2 工程 Svn 版本号
  private fun readPlatformCodeSvnVersion() {
    //return com.xlcwnet.sdk.dock.compat.BuildConfig.B2_SVN_VERSION_CODE
  }

  //检查 dex 方法数
  private fun readMethodRefs(): Int {
    return 0
  }

  //检查微信支付 Activity 是否存在
  private fun weChatActivityExist(): Boolean {
    apkFile.dexClasses.forEach {
      println("classType: ${it.classType}, packageName: ${it.packageName}")
      if (it.classType.substring(1, it.classType.length - 1)
          .replace("/", ".") == "${paraMap[packageName]}.wxapi.WXPayEntryActivity"
      ) return true
    }
    return false
  }

  //检查统一配置地址是否有效
  fun checkConfigUrl(paraMap: Map<String, String>) {
    val configUrl = paraMap[configUrl]
    val paramString: String?
    
    val params = TreeMap<String, String>()
    params["bin_ver"] = paraMap[versionName]!!
    params["bin_name"] = paraMap[packageName]!!
    params["uuid"] = "00000000-0000-0000-0000-000000000000"
    if (TextUtils.isEmpty(paraMap["configUrlSalt"])) {
      params["channel"] = paraMap[channelId]!!
      paramString = signParamWithSalt(params, null)
    } else {
      params["channel_id"] = paraMap[channelId]!!
      params["timestamp"] = (System.currentTimeMillis() / 100).toString()
      paramString = signParamWithSalt(params, paraMap["configUrlSalt"])
    }


    val client = OkHttpClient()
    val request = Request.Builder()
      .url("${configUrl}?${paramString}")
      .build()

    showAlert("request url: ${request.url}")
    showAlert("response: ${client.newCall(request).execute().body?.string()}")
  }

  //读取图标
  private fun readIcons() {
    apkFile.allIcons.forEach { iconFace ->
      println("iconPath: ${iconFace.path}")
    }
  }

  private fun readCertificationMd5() {
    apkFile.apkSingers.forEach { signer ->
      signer.certificateMetas.forEach { certificateMeta ->
        println(
          "v1 signer - ${signer.path} ---> " +
              "\n\t signAlgorithm: ${certificateMeta.signAlgorithm}" +
              "\n\t signAlgorithmOID: ${certificateMeta.signAlgorithmOID}" +
              "\n\t startDate: ${certificateMeta.startDate}" +
              "\n\t endDate ${certificateMeta.endDate}" +
              "\n\t data ${certificateMeta.data}" +
              "\n\t certBase64Md5: ${certificateMeta.certBase64Md5}" +
              "\n\t certMd5: ${certificateMeta.certMd5}"
        )
      }
    }

    apkFile.apkV2Singers.forEach { signer ->
      signer.certificateMetas.forEach { certificateMeta ->
        println(
          "apkV2Signer - certificateMeta: " +
              "\n\t signAlgorithm: ${certificateMeta.signAlgorithm}" +
              "\n\t signAlgorithmOID: ${certificateMeta.signAlgorithmOID}" +
              "\n\t startDate: ${certificateMeta.startDate}" +
              "\n\t endDate ${certificateMeta.endDate}" +
              "\n\t data ${certificateMeta.data}" +
              "\n\t certBase64Md5: ${certificateMeta.certBase64Md5}" +
              "\n\t certMd5: ${certificateMeta.certMd5}"
        )
      }
    }
  }
}