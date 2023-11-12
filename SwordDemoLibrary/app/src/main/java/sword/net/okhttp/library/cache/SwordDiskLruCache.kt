package sword.net.okhttp.library.cache

import okio.Source


class SwordDiskLruCache(
    val fileCountPerEntry: Int
) {
    internal val diskLruEntries = LinkedHashMap<String, Entry>()
    fun get(key: String): SnapShot? {
        val entry = diskLruEntries[key]
        return entry?.snapshot()
    }

    inner class SnapShot(
        val sources: List<Source>
    ) {
    }

    internal inner class Entry {
        fun snapshot(): SnapShot {
            val sources = mutableListOf<Source>()
            //todo: 获取缓存文件的 Source 对象
            return SnapShot(sources)
        }
    }
}