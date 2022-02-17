package com.example.utilclass.net.dongliu.apk.parser.xlcw

//运营固定参数
val operationFixParam = listOf(
    "platform",
    "channel",
    "minSdkVersion",
    "targetSdkVersion",
    "keyStore"
)


const val appName = "appName"
const val packageName = "packageName"
const val versionName = "versionName"
const val versionCode = "versionCode"
const val minSdkVersion = "minSdkVersion"
const val targetSdkVersion = "targetSdkVersion"
const val configUrl = "ConfigUrl"
const val channelId = "Xl_Channel_ID"
const val ConfigUrlSalt = "ConfigUrlSalt"


//技术固定参数
val techFixParam = listOf(
    configUrl,
    "svnVersion",
    "XL_Game_Id",
    "XL_DATA_URL",
    "XL_DATA_AREA",
    "XL_DATA_ISIMEI",
    "XL_SDK_VERSION_CODE",
    "XL_SDK_SHOW_SPLASH",
    "XL_SDK_SIGN",
    "SDKType",
    "VersionNum"
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
val configParam = listOf(
    "需求时间",
    "运营负责人",
    "完成时间",
    "平台负责人",
    "测试负责人",
    "完成情况",
    "备注说明",
    "系统",
    "渠道名称",
    "游戏名称",
    "包名",
    "渠道号",
    "子渠道号",
    "apk 版本号",
    "二进制版本号"
)

/*
    AppId：第三方 SDK 提供的 key
    AppKey：第三方 SDK 提供的 Secret
    TouTiao_App_Id：头条 AppId
    TouTiao_App_Name：头条 appName
    ReYun_App_Id：热云 appid
    XL_BUGLY_APPID：bugly appid
 */
val sdkParam = listOf(
    "AppId",
    "AppKey",
    "TouTiao_App_Id",
    "TouTiao_App_Name",
    "ReYun_App_Id",
    "XL_BUGLY_APPID"
)
