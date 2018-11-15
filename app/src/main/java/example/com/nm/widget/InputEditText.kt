package example.com.nm.widget

import android.content.Context
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import example.com.nm.R
import example.com.nm.util.extensions.emptyToNull
import example.com.nm.util.extensions.setTextOrHide
import kotlinx.android.synthetic.main.view_input_edit_text.view.*


open class InputEditText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): LinearLayout(context, attrs, defStyleAttr) {

    var text: String
        get() = editText.text.toString()
        set(text) = editText.setText(text)

    init {
        initializeLayoutBasics(context, attrs)
    }

    protected open fun getLayout() =  R.layout.view_input_edit_text

    protected open fun initializeLayoutBasics(context: Context, attrs: AttributeSet?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(getLayout(), this)
        orientation = LinearLayout.VERTICAL


        isFocusable = true
        isFocusableInTouchMode = true

        val array = getContext().obtainStyledAttributes(attrs, R.styleable.InputEditText)

        txtLabel.setTextOrHide(array.getText(R.styleable.InputEditText_label)?.toString().emptyToNull())

        editText.isEnabled = array.getBoolean(R.styleable.InputEditText_enabled, true)
        editText.hint = array.getText(R.styleable.InputEditText_hint)
        editText.setText(array.getText(R.styleable.InputEditText_text))


        val resource = array.getResourceId(R.styleable.InputEditText_editStyle, -1)
        if (resource > 0)
            editText.setTextAppearance(getContext(), resource)

        editText.inputType = array.getInt(R.styleable.InputEditText_android_inputType, InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
        editText.nextFocusDownId = array.getResourceId(R.styleable.InputEditText_android_nextFocusDown, View.NO_ID)
        editText.imeOptions = array.getResourceId(R.styleable.InputEditText_android_imeOptions, EditorInfo.IME_ACTION_UNSPECIFIED)

        array.recycle()
    }


    fun setRightDrawable(drawable: Int) {
        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
    }

    fun setEnable(enable: Boolean) {
        editText.isEnabled = enable
        txtLabel.setTextColor(ContextCompat.getColor(editText.context, if (enable) R.color.gray else R.color.gray3))
        editText.setTextColor(ContextCompat.getColor(editText.context, if (enable) R.color.gray1 else R.color.gray3))
    }

    override fun isEnabled(): Boolean {
        return editText.isEnabled
    }

    fun getEditText(): EditText? {
        return editText
    }

    fun setHint(hind: String) {
        editText.hint = hind
    }

    fun setSelection(selection: Int) {
        editText.setSelection(selection)
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean {
        return editText.requestFocus(direction, previouslyFocusedRect)
    }

    override fun setOnFocusChangeListener(listener: View.OnFocusChangeListener?) {
        if (listener == null)
            editText.onFocusChangeListener = null
        else {
            editText.setOnFocusChangeListener { view, focus ->
                listener.onFocusChange(this, focus)
            }
        }
    }
}
