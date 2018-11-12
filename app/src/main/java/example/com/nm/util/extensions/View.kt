package example.com.nm.util.extensions

import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.View
import android.view.ViewGroup

inline fun View.visible(visible: Boolean = false) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

inline fun View.hide() {
    visibility = View.GONE
}

inline fun View.show() {
    visibility = View.VISIBLE
}

inline fun View.invisible() {
    visibility = View.INVISIBLE
}

inline fun View.isVisible() = visibility == View.VISIBLE

inline fun View.setMarginTop(margin: Int) {
    val marginLayout = layoutParams as? ViewGroup.MarginLayoutParams ?: return

    marginLayout.topMargin = margin
}

inline fun View.setMarginBottom(margin: Int) {
    val marginLayout = layoutParams as? ViewGroup.MarginLayoutParams ?: return

    marginLayout.bottomMargin = margin
}

inline fun View.setMarginLeft(margin: Int) {
    val marginLayout = layoutParams as? ViewGroup.MarginLayoutParams ?: return

    marginLayout.leftMargin = margin
}

inline fun View.setMarginRight(margin: Int) {
    val marginLayout = layoutParams as? ViewGroup.MarginLayoutParams ?: return

    marginLayout.rightMargin = margin
}

inline fun View.setHeight(height: Int) {
    layoutParams.height = height
}

inline fun View.snack(@StringRes messageRes: Int, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    snack(resources.getString(messageRes), length, f)
}

inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

fun Snackbar.action(@StringRes actionRes: Int, color: Int? = null, listener: (View) -> Unit) {
    action(view.resources.getString(actionRes), color, listener)
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}