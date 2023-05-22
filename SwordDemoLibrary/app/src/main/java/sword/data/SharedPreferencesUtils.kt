package sword.data

import android.content.Context

class SharedPreferencesUtils {
  companion object {
    private const val usersPrefs = "users"
    
    fun put(context: Context, username: String, password: String) {
      val spEditor = context.getSharedPreferences(usersPrefs, Context.MODE_PRIVATE).edit()
      spEditor.putString(username, password).apply()
    }
    
    fun get(context: Context, key: String, defaultValue: Any): Any {
      val sp = context.getSharedPreferences(usersPrefs, Context.MODE_PRIVATE)
      return when(defaultValue) {
        is String -> sp.getString(key, defaultValue)!!
        is Boolean -> sp.getBoolean(key, defaultValue)
        is Float -> sp.getFloat(key, defaultValue)
        is Int -> sp.getInt(key, defaultValue)
        is Long -> sp.getLong(key, defaultValue)
        else -> { defaultValue }
      }
    }
  }
}