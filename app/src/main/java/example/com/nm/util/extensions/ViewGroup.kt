package example.com.nm.util.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import example.com.nm.util.Util


inline fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

inline fun ViewGroup.findByTag(tag: String): List<View> = Util.getViewsByTag(this, tag)