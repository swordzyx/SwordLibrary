package sword.utils;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import sword.logger.SwordLog;

public class XmlDecoder {
    private static final String tag = "XmlDecoder";

    /**
     * 解析 xml 内容
     */
    public static void decode(String xmlContent) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        InputStream input = new ByteArrayInputStream(xmlContent.getBytes());
        parser.setInput(input, "UTF-8");
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    SwordLog.debug(tag, "start document");
                    break;
                case XmlPullParser.START_TAG:
                    SwordLog.debug(tag, "start tag, " + parser.getName());
                    int attributeCount = parser.getAttributeCount();
                    for (int i = 0; i<attributeCount; i++) {
                        String name = parser.getAttributeName(i);
                        String value = parser.getAttributeValue(i);
                        SwordLog.debug(tag, "attribute: " + name + " = " + value);
                    }
                    break;
                case XmlPullParser.TEXT:
                    SwordLog.debug(tag, "text: " + parser.getText());
                case XmlPullParser.END_TAG:
                    SwordLog.debug(tag, "start tag, " + parser.getName());
                    break;
                default:
                    break;
            }
            eventType = parser.next();
        }
    }
}
