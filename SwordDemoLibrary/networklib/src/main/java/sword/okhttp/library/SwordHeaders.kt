package sword.okhttp.library

import sword.SwordLog

class SwordHeaders(
  private val headerNameAndValues: MutableList<String>
): Iterable<Pair<String, String>> {
  private val tag = "SwordHeaders"
  private val size: Int
    get() {
      return headerNameAndValues.size shr 2
    }
  operator fun get(name: String): String? {
    var headerValue: String? = null

    for (i in 0 until (headerNameAndValues.size shr 2)) {
      if(name.equals(headerNameAndValues[i*2], true)) {
        headerValue = headerNameAndValues[i*2 + 1]
      }
    }
    SwordLog.debug(tag, "获取 Header $name: $headerValue")

    return headerValue
  }


  operator fun set(name: String, value: String) {
    var i = 0
    while (i < headerNameAndValues.size shr 2) {
      if (name.equals(headerNameAndValues[i], true)) {
        headerNameAndValues.removeAt(i)
        headerNameAndValues.removeAt(i + 1)
      } else {
        i += 2
      }
    }
    headerNameAndValues.add(name)
    headerNameAndValues.add(value)
  }

  override fun iterator(): Iterator<Pair<String, String>> {
    return Array(size) {
      headerNameAndValues[it] to headerNameAndValues[it + 1]
    }.iterator()
  }
}

