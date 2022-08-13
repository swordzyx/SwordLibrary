package com.example.connectivity

import android.app.Activity
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.webkit.WebView
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat

class NetworkActivity : Activity() {
    private lateinit var recevier: NetworkReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        recevier = NetworkReceiver()
    }
    
    companion object {
        const val WIFI = "Wi-Fi"
        const val ANY = "Any"
        const val SO_URL = "http://stackoverflow.com/feeds/tag?tagnames=android&sort=newest"

        private var wifiConnected = false
        private var mobileConnected = false

        var refreshDisplay = true
        var sPref: String? = null
    }

    fun loadPage() {
        if (sPref.equals(ANY) && (wifiConnected || mobileConnected)) {
            DownloadXmlTask().execute(SO_URL)
        } else if(sPref.equals(WIFI) && wifiConnected) {
            DownloadXmlTask().execute(SO_URL)
        } else {
            //没有网络权限，无法拉取 xml 数据源
        }
    }



    private inner class DownloadXmlTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg urls: String): String {
            return try {
                loadXmlFromNetwork(urls[0])
            } catch (e: IOException) {
                "connection_error"
            } catch (e: XmlPullParserException) {
                "xml_error"
            }
        }

        override fun onPostExecute(result: String?) {
            setContentView(R.layout.main)
            findViewById<WebView>(R.id.webview)?.apply {
                loadData(result, "text/html", null)
            }
        }
    }
    val formatter = SimpleDateFormat.getDateInstance()

    /**
     * 1. 实例化 StackOverflowXmlParser，创建 Entry 列表
     * 2. 执行 downloadUrl() ，获取数据源并返回一个 InputStream
     * 3. 使用 StackOverflowParser 解析 InputStream
     * 4. 处理 Entry 列表的数据，将其与 HTML 标记结合
     * 5. 返回一个 HTML 字符串到 onPostExcute() 方法中，显示在 UI 上
     */
    private fun loadXmlFromNetwork(urlString: String): String {
        val pref: Boolean = PreferenceManager.getDefaultSharedPreferences(this)?.run {
            getBoolean("summaryPref", false)
        } ?: false

        val entries: List<ParseXmlData.Entry> = downloadUrl(urlString).use { stream ->
            ParseXmlData().parse(stream) ?: emptyList()
        }

        return StringBuilder().apply {
            append("<h3>${resources.getString(R.string.page_title)}</h3>")
            append("<em>${resources.getString(R.string.updated)} ")
            append("${formatter.format(System.currentTimeMillis())}</em>")

            entries.forEach { entry ->
                append("<p><a href='")
                append(entry.link)
                append("'>" + entry.title + "</a></p>")
                if (pref) {
                    append(entry.summary)
                }
            }
        }.toString()
    }


    private fun downloadUrl(urlString: String): InputStream? {
        val url = URL(urlString)
        return (url.openConnection() as? HttpURLConnection)?.run {
            readTimeout = 10000
            connectTimeout = 15000
            requestMethod = "GET"
            doInput = true
            connect()
            inputStream
        }
    }
}