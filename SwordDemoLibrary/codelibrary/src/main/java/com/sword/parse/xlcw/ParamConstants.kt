package com.example.utilclass.apk.parse.xlcw

//运营固定参数
val operFixParam = mapOf(
    "platform" to "",
    "channel" to "Android",
    "minVersion" to "",
    "targetSdkVersion" to "",
    "keyStore" to ""
)

const val VersionNum = "VersionNum"

//技术固定参数
val techFixParam = mapOf(
    "configUrl" to "",
    "svnVersion" to "",
    "XL_Game_Id" to "",
    "XL_DATA_URL" to "",
    "XL_DATA_AREA" to "",
    "XL_DATA_ISIMEI" to "",
    "XL_SDK_VERSION_CODE" to "",
    "XL_SDK_SHOW_SPLASH" to "",
    "XL_SDK_SIGN" to "",
    "SDKType" to "",
    VersionNum to ""
)

/**
 * 出包配置参数
 * 1. 出包时间
 * 2. 运营负责人
 * 3. 完成时间
 * 4. 平台负责人
 * 5. 测试负责人
 * 6. 完成情况
 * 7. 备注说明
 * 8. 平台（默认为 Android）
 * 9. 子渠道名称
 * 10. 游戏名称
 * 11. 包名
 * 12. 渠道号（Xl_Channel_ID）
 * 13. 子渠道号（CCHId）
 * 14. apk 版本号（versionCode）
 * 15. 二进制版本号（versionName）
 */
val configParam = mapOf(
    "需求时间" to "",
    "运营负责人" to "",
    "完成时间" to "",
    "平台负责人" to "",
    "测试负责人" to "",
    "完成情况" to "",
    "备注说明" to "",
    "系统" to "Android",
    "渠道名称" to "",
    "游戏名称" to "",
    "包名" to "",
    "渠道号" to "",
    "子渠道号" to "",
    "apk 版本号" to "",
    "二进制版本号" to ""
)

/*
    AppId：第三方 SDK 提供的 key
    AppKey：第三方 SDK 提供的 Secret
    TouTiao_App_Id：头条 AppId
    TouTiao_App_Name：头条 appName
    ReYun_App_Id：热云 appid
    XL_BUGLY_APPID：bugly appid
 */
val sdkParam = mapOf(
    "AppId" to "",
    "AppKey" to "",
    "TouTiao_App_Id" to "",
    "TouTiao_App_Name" to "",
    "ReYun_App_Id" to "",
    "XL_BUGLY_APPID" to ""
)
