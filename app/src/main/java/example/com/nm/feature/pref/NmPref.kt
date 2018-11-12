package example.com.nm.feature.pref

import android.content.Context
import android.content.SharedPreferences
import example.com.nm.R
import example.com.nm.feature.home.domain.entity.UserData
import io.paperdb.Book
import io.paperdb.Paper

class NmPref constructor(context: Context) {

    private val paper: Book
    private val sharedPref: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.preference_file_key),
        Context.MODE_PRIVATE)

    init {
        Paper.init(context)
        paper = Paper.book()
    }

    var loggedUser: UserData?
        set(userData) { paper.write(Consts.USER_DATA, userData) }
        get() = paper.read<UserData>(Consts.USER_DATA)


    operator fun set(key: String, value: Any?) {
        val editor = sharedPref.edit()

        when (value) {
            is Boolean -> editor.putBoolean(key, value)
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Long -> editor.putLong(key, value)
            is Float -> editor.putFloat(key, value)
            is Double -> editor.putFloat(key, value.toFloat())
        }

        editor.apply()
    }

    fun getString(key: String, strdefault: String? = ""): String? {
        return sharedPref.getString(key, strdefault)
    }

    fun getInt(key: String, valuedefault: Int): Int {
        return sharedPref.getInt(key, valuedefault)
    }

    fun getLong(key: String, valuedefault: Long): Long {
        return sharedPref.getLong(key, valuedefault)
    }

    fun getBoolean(key: String, defaultvalue: Boolean): Boolean {
        return sharedPref.getBoolean(key, defaultvalue)
    }

    fun getFloat(key: String, valuedefault: Float): Float {
        return sharedPref.getFloat(key, valuedefault)
    }

    fun remove(key: String) {
        val editor = sharedPref.edit()
        editor.remove(key)

        editor.apply()
    }

    fun clean() {
        val editor = sharedPref.edit()
        editor.clear()

        Paper.book().destroy()
        editor.apply()
    }
}