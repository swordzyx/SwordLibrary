package com.example.utilclass.net.dongliu.apk.parser

import junit.framework.TestCase

class ApkReaderTest : TestCase() {
  val reader = ApkReader("E:\\B2_ope_China2_Release_IL2CPP_xlcw_3.1.35_202201251209_xlcw_webpay_hsyw_74.1.35_202202151803_l1096.apk")
  
  fun testReadPlatformCodeSvnVersion() {
    //Lcom/xwgame/best2/hsl1095/wxapi/WXPayEntryActivity;
    //pakcageName: com.xwgame.best2.hsl1095.wxapi
    //reader.readPlatformCodeSvnVersion()
  }
}