package com.example.connectivity

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

private val ns: String? = null
class ParseXmlData {

    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream?): List<Entry> {
        inputStream.use { inputStream ->
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            //开始解析过程
            parser.nextTag()
            //提取并处理数据
            return readFeed(parser)
        }
    }

    private fun readFeed(parser: XmlPullParser): List<Entry> {
        val entries = mutableListOf<Entry>()

        parser.require(XmlPullParser.START_TAG, ns, "feed")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            //查找 entry 标签
            if (parser.name == "entry") {
                entries.add(readEntry(parser))
            } else {
                skip(parser)
            }
        }

        return entries
    }

    private fun readEntry(parser: XmlPullParser): Entry {
        parser.require(XmlPullParser.START_TAG, ns, "entry")
        var title: String? = null
        var summary: String? = null
        var link: String? = null
        //移动到下一个事件
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "title" -> title = readTitle(parser)
                "summary" -> summary = readSummary(parser)
                "link" -> link = readLink(parser)
                else -> skip(parser)
            }
        }

        return Entry(title, summary, link)
    }

    private fun readTitle(parser: XmlPullParser): String? {
        //检查当前的 event 是否为起始标签，且标签名为 "title"
        parser.require(XmlPullParser.START_TAG, ns, "title")
        val title = readText(parser)
        //检查当前 event 是否为结束标签，且标签名为 "title"
        parser.require(XmlPullParser.END_TAG, ns, "title")
        return title
    }

    private fun readSummary(parser: XmlPullParser): String? {
        parser.require(XmlPullParser.START_TAG, ns, "summary")
        val summary = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "summary")
        return summary
    }

    private fun readLink(parser: XmlPullParser): String? {
        parser.require(XmlPullParser.START_TAG, ns, "link")
        var link: String? = null
        //判断当前标签名是否为 "link"，且该标签的属性是否为 "alternate"
        if (parser.name == "link" && parser.getAttributeValue(null, "rel") == "alternate") {
            link = parser.getAttributeValue(null, "href")
            parser.nextTag()
        }
        parser.require(XmlPullParser.END_TAG, ns, "link")
        return link
    }

    private fun readText(parser: XmlPullParser): String {
        var text = ""
        //如果下一个解析的事件是文本事件
        if (parser.next() == XmlPullParser.TEXT) {
            //返回当前标签的文本内容
            text = parser.text
            parser.nextTag()
        }
        return text
    }

    private fun skip(parser: XmlPullParser) {
        if(parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }

        //如果当前标签下面有自标签，则 depth 一定不会为 0。一个开始标签对应一个结束标签，二者互相抵消，当开始标签的数量和结束标签的数量相等，说明此标签已经结束了。
        var depth = 1
        while (depth != 0) {
            when(parser.next()) {
                XmlPullParser.START_TAG -> ++depth
                XmlPullParser.END_TAG -> --depth
            }
        }
    }

    data class Entry(val title: String?, val summary: String?, val link: String?)

}