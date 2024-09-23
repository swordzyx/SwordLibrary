package sword;

import java.net.InetAddress;

/**
 * [InetAddress](<a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/net/InetAddress.html">Java InetAddress</a>) 类的用法
 */
public class InetAddressUtil {
    public String getIP() {
        return InetAddress.getLoopbackAddress().getHostAddress();
    }
}
