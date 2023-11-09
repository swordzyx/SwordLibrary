package sword.net.okhttp.library

class SwordHeaders(
  private val headerNameAndValues: Array<String>
): Iterable<Pair<String, String>> {
  
  operator fun get(name: String): String? = ""
  
  fun 
  
  operator fun set(name: String, value: String) {
    
  }
  
  override fun iterator(): Iterator<Pair<String, String>> {
    TODO("Not yet implemented")
  }
}