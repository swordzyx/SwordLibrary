package sword.net.okhttp.library

import sword.net.okhttp.library.connect.SwordConnection

/**
 * 表示网络交互操作这个行为
 * 发生一次网络交互需要网络连接（Connection）、请求（或响应）的具体内容、交互的目标主机（Address）
 */
class SwordExchangeFinder(
  connectionPool: SwordConnectionPool,
  call: SwordRealCall,
  address: SwordAddress
) {
  //todo：找到一个可用的连接
  //todo：拿到一个可以在这个连接中使用的编解码器
  fun find(): SwordExchangeCodec {

  }

  private fun findHealthConnection(): SwordConnection {
    //不断尝试，直到找到一个可用的连接或者确认找不到一个可用的连接为止
    while (true) {
      //todo：判断上一次的连接是否可以被重用，主机名和端口号需要一样，

      //todo：尝试从连接池获取一个非多路复用并且可用的连接（即 HTTP 1.1 连接）
      //todo：拿到一个通往目标主机的路由列表，再次尝试获取 HTTP1.1 连接，或者基于路由列表查找一个 HTTP2/3 的可用连接
      //todo：创建一个新的连接
      
      //todo： 
      // 可用的标准：
      // 1. 这个连接愿意接收新的请求
      // 2. 这个连接上正在处理的请求数没有超过上限；目标主机名和端口号要一致；Dns 要一样；使用的 TLS 协议配置要一样；代理要一样；连接除主机名之外的相关配置都要一样
      // 3. 针对主机名不一样的情况，判断是否是 HTTP 2/3 的连接，因为这支持连接合并
      //  HTTP 2 以上支持合并的标准：
      //    1. 没有使用任何代理服务器，可复用的连接的目标主机IP应和原始请求的目标主机的 IP 地址一致
      //    2. 使用 RFC2818 这个标准来校验主机，RFC2818，即在 TLS 相关配置中需要有目标主机名
      //    3. 可复用连接的路由指向的目标IP和端口号要和原始请求的目标IP和端口号一致
      //    4. 连接里面所包含的证书要和请求的主机的证书要一致
      //    5. 开发者针对目标主机固定的证书要包含在连接的证书链中

    }
  }

}


/**
 * 编解码器
 */
class SwordExchangeCodec {

}