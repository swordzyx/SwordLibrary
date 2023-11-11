package sword.net.okhttp.library.cache

import com.bumptech.glide.disklrucache.DiskLruCache
import okhttp3.Response
import sword.net.okhttp.library.SwordRequest

class SwordCache {
  private val entries: LinkedHashMap<String, Entry> = LinkedHashMap() 
  private val diskLruCache: SwordDiskLruCache = SwordDiskLruCache()
  
  public fun get(request: SwordRequest): Response? {
    val result = null
    //获取缓存的 Entry
    
    //将 request 转成 key
    
    //通过 key 从磁盘缓存中获取 entry
    
    //将 entry 转成 Response
    
    return result
  }
  
  internal inner class Entry(val key: String) {
    
  }
}
