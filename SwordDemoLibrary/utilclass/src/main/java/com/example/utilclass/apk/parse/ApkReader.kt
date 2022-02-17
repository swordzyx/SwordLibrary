package com.example.utilclass.apk.parse


private val tempDir = "tempDir/"
private val platformConfigPath = "assets/platform_config.properties"
private val versionBytePath = "assets/AssetBundles/Android/version.bytes"
private val androidManifestPath = "AndroidManifest.xml"

class ApkReader(apkPath: String) {
  private val apkFile = ApkFile(apkPath)

  //读取 *.properties 文件
  private fun readPropertyFile(filePath: String): List<Property> {
    println("---------------- start read $filePath content ------------------")
    val stringContent = apkFile.getStringFileData(filePath) ?: return emptyList()

    return mutableListOf<Property>().apply {
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
          if (equalIndex == lineWithoutSpace.length - 1) "" else lineWithoutSpace.substring(equalIndex + 1..lineWithoutSpace.length)
        add(Property(key, value))
      }
      println("----------------------- end read $filePath --------------------------")
    }
  }

  //读取内容为 json 格式的文件
  private fun readJsonFile(filePath: String): List<Property> {
    println("---------------- start read $filePath content ------------------")
    val stringContent = apkFile.getStringFileData(filePath)?.apply {
      substring(1 until length)
    } ?: return emptyList()

    println("$filePath content: $stringContent")

    return mutableListOf<Property>().apply {
      stringContent
        .replace("\"", "")
        .split(",")
        .forEach {
          val propertyString = it.split(":")

          if (propertyString.size == 2)
            add(Property(propertyString[0], propertyString[1]))
        }

      println("---------------- end read $filePath ------------------")
    }
  }


  //读取 xml 文件
  private fun readXmlFile(filePath: String) {
    
  }
  
  
  
}