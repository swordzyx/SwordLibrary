package sword.net.okhttp.library

import java.lang.Exception

fun SwordEventListener.asFactory() = SwordEventListener.SwordEventFactory { this }

abstract class SwordEventListener {
  open fun callStart(call: SwordRealCall) {
    
  }
  
  open fun callEnd(call: SwordRealCall) {
    
  }
  
  open fun callFailed(call: SwordRealCall, e: Exception) {
    
  }
  
  fun interface SwordEventFactory {
    fun create(call: SwordRealCall): SwordEventListener
  }
}