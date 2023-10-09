package sword.kotlin

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


//(Continuation) -> Any?
suspend fun getUserInfo(): String {
  withContext(Dispatchers.IO) {
    delay(1000)
  }
  return "BoyCoder"
}

//(String, Continuation) -> Any?
suspend fun getFriendList(user: String): String {
  withContext(Dispatchers.IO) {
    delay(1000)
  }

  return "Tom, Jack"
}


//(String, Continuation) -> String
suspend fun getFeedList(list: String): String {
  withContext(Dispatchers.IO) {
    delay(1000)
  }
  return "{FeedList..}"
}

fun main() = runBlocking {
  val user = getUserInfo()
  val friendList = getFriendList(user)
  val feedList = getFeedList(friendList)

  println("feedList: $feedList")
}

class SuspendTest {
}