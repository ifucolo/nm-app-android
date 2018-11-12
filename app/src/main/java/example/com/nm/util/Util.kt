package example.com.nm.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import com.google.gson.JsonParser
import example.com.nm.feature.home.domain.entity.ChargePoint
import example.com.nm.util.extensions.readFiletoString
import java.io.IOException
import java.util.ArrayList

object Util {

    fun getViewsByTag(root: ViewGroup, tag: String?): ArrayList<View> {
        val views = ArrayList<View>()
        val childCount = root.childCount
        for (i in 0 until childCount) {
            val child = root.getChildAt(i)
            if (child is ViewGroup) {
                views.addAll(getViewsByTag(child, tag))
            }

            val tagObj = child.tag
            if (tagObj != null && tagObj == tag) {
                views.add(child)
            }

        }
        return views
    }

    fun closeInput(caller: View?, cleanFocus: Boolean) {
        if (caller == null)
            return

        caller.postDelayed({
            val imm = caller.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(caller.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

            if (cleanFocus)
                caller.clearFocus()
        }, 100)
    }

    fun openUri(context: Context, linkUrl: String) {
        val sendIntent = Intent("android.intent.action.VIEW")
        sendIntent.data = Uri.parse(linkUrl)
        context.startActivity(sendIntent)
    }

    fun getChargePoints(context: Context) = ArrayList<ChargePoint>().apply {
        try {
            val stringJson = context.readFiletoString("json/sample-json-chargepoints.json")
            val jsonArray = JsonParser().parse(stringJson).asJsonArray

            jsonArray.forEach { jsonObject ->
                add(Gson().fromJson(jsonObject, ChargePoint::class.java))
            }
        } catch (ex: IOException) {

        }
    }

}