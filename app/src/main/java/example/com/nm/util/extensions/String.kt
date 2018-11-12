package example.com.nm.util.extensions

inline fun String?.emptyToNull() = if (isNullOrEmpty()) null else this

