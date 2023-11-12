package sword.net.okhttp.library.cache

import okhttp3.Response
import okio.ByteString.Companion.encodeUtf8
import sword.net.okhttp.library.SwordHttpUrl
import sword.net.okhttp.library.SwordRequest
import java.io.IOException

class SwordCache private constructor(){
  private val entries: LinkedHashMap<String, Entry> = LinkedHashMap() 
  private val diskLruCache: SwordDiskLruCache = SwordDiskLruCache()
  
  fun get(request: SwordRequest): Response? {
    val result = null

    val key = key(request.url)
    //获取缓存的 Entry
    try {
      val diskLruCache.get(key) ?: return null
    } catch (e: IOException) {
      e.printStackTrace()
    }
    

    //通过 key 从磁盘缓存中获取 entry
    
    //将 entry 转成 Response
    
    return result
  }
  
  internal inner class Entry(val key: String) {
    
  }

  private fun key(url: SwordHttpUrl): String {
    return url.toString().encodeUtf8().md5().hex()
  }

  companion object {
    val instance = SwordCache()
  }
}
