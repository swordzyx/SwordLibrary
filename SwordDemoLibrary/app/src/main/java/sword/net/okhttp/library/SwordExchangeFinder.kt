package sword.net.okhttp.library

/**
 * 表示网络交互操作这个行为
 * 发生一次网络交互需要网络连接（Connection）、交互的具体内容（Call）、交互的目标主机（Address）
 */
class SwordExchangeFinder(
  connectionPool: SwordConnectionPool,
  call: SwordRealCall,
  address: SwordAddress
) {
  
}