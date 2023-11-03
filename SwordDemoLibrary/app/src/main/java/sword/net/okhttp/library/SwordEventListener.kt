package sword.net.okhttp.library

fun SwordEventListener.asFactory() = SwordEventListener.SwordEventFactory { this }

abstract class SwordEventListener {
  open fun callStart(call: SwordRealCall) {
    
  }
  
  fun interface SwordEventFactory {
    fun create(call: SwordRealCall): SwordEventListener
  }
}