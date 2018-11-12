package example.com.nm.util.extensions

import android.app.ProgressDialog
import android.content.Context
import android.support.annotation.StringRes
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import example.com.nm.R
import java.io.IOException
import java.nio.charset.Charset

/**
 * Created by bruno on 07/07/17.
 */
inline fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

inline fun Context.toast(@StringRes resource: Int, duration: Int = Toast.LENGTH_SHORT) = toast(getString(resource), duration)

inline fun Context.inflate(layoutId: Int, parent: ViewGroup, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(this).inflate(layoutId, parent, attachToRoot)
}

inline fun Context.dpToPx(dp: Float) = Math.round(dp * resources.displayMetrics.density)

inline fun Context.alert(func: AlertDialog.Builder.() -> AlertDialog.Builder) {
    AlertDialog.Builder(this, R.style.AppTheme).func().show()
}

inline fun Context.progressDialog(message: Int, title: Int? = null) = ProgressDialog.show(this, title?.let { getString(it) }, getString(message))
inline fun Context.progressDialog(message: CharSequence, title: CharSequence? = null) = ProgressDialog.show(this, title, message)

inline fun Context.readFiletoString(path: String): String? {
    return try {
        val asset = assets.open(path)
        val size = asset.available()
        val buffer = ByteArray(size)
        asset.read(buffer)
        asset.close()

        String(buffer, Charset.forName("UTF-8"))
    } catch (io: IOException) {
        null
    }
}
