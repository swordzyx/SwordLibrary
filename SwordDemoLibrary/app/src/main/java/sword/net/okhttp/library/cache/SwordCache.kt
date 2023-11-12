package sword.net.okhttp.library.cache

import okhttp3.Response
import okio.ByteString.Companion.encodeUtf8
import okio.Source
import sword.net.okhttp.library.SwordHttpUrl
import sword.net.okhttp.library.SwordRequest
import sword.net.okhttp.library.SwordResponse
import java.io.IOException

class SwordCache private constructor(){
  private val entries: LinkedHashMap<String, Entry> = LinkedHashMap() 
  private val diskLruCache: SwordDiskLruCache = SwordDiskLruCache(
    2
  )
  
  fun get(request: SwordRequest): SwordResponse? {
    val result = null

    val key = key(request.url)
    //todo: 从磁盘中取出缓存的响应报文的快照，对这个快照执行任何操作都不会影响本地的缓存
    val snapshot = try {
      diskLruCache.get(key) ?: return null
    } catch (e: IOException) {
      e.printStackTrace()
    }

    //todo: 将 snapshot 转为 Cache.Entry，稍后要通过 Cache.Entry 构造响应报文

    
    //todo: 将 entry 转成 Response
    
    return result
  }
  
  internal inner class Entry(
    sources: List<Source>
  ) {
    
  }

  private fun key(url: SwordHttpUrl): String {
    return url.toString().encodeUtf8().md5().hex()
  }

  companion object {
    val instance = SwordCache()
  }
}
