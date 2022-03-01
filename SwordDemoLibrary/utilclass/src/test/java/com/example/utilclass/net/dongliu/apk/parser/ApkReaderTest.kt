package com.example.utilclass.net.dongliu.apk.parser

import com.example.utilclass.net.dongliu.apk.parser.xlcw.channelId
import com.example.utilclass.net.dongliu.apk.parser.xlcw.configUrl
import com.example.utilclass.net.dongliu.apk.parser.xlcw.packageName
import com.example.utilclass.net.dongliu.apk.parser.xlcw.versionName
import junit.framework.TestCase
import org.junit.Test

class ApkReaderTest {
  val reader = ApkReader("E:\\B2_ope_China2_Release_IL2CPP_xlcw_3.1.35_202201251209_xlcw_webpay_hsyw_74.1.35_202202151803_l1096.apk")
  
  fun testReadPlatformCodeSvnVersion() {
    //Lcom/xwgame/best2/hsl1095/wxapi/WXPayEntryActivity;
    //pakcageName: com.xwgame.best2.hsl1095.wxapi
    //reader.readPlatformCodeSvnVersion()
  }
  
  @Test
  fun testCheckConfigUrl() {
    val params = mapOf(
      configUrl to "http://136.243.234.173:13006/Config/v2",
      versionName to "1.0.1",
      packageName to "com.espritgames.rpg.nine.songs",
      channelId to "R1001",
      "configUrlSalt" to "be1ab1632e4285edc3733b142935c60b"
    )
    
    reader.checkConfigUrl(params)
  }
}