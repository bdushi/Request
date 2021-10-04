package com.example.twitter

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import com.google.android.material.textfield.TextInputLayout


class MyTextInputLayout : TextInputLayout {
    private val normal: Drawable?
    private val error: Drawable?
    private val appAttrs: TypedArray
//    private var states = arrayOf(
//        intArrayOf(android.R.attr.state_pressed),
//        intArrayOf(android.R.attr.state_focused),
//        intArrayOf(android.R.attr.state_selected),
//        intArrayOf(android.R.attr.state_checkable),
//        intArrayOf(android.R.attr.state_checked),
//        intArrayOf(android.R.attr.state_enabled),
//        intArrayOf(android.R.attr.state_window_focused)
//    )
private var states = arrayOf(
    intArrayOf(android.R.attr.state_pressed),
    intArrayOf(android.R.attr.state_focused),
    intArrayOf(android.R.attr.state_selected),
    intArrayOf(android.R.attr.state_checkable),
    intArrayOf(android.R.attr.state_checked),
    intArrayOf(android.R.attr.state_enabled),
    intArrayOf(android.R.attr.state_window_focused)
)

    private var colors = intArrayOf(
        Color.GREEN,
        Color.GREEN,
        Color.GREEN,
        Color.GREEN,
        Color.GREEN,
        Color.GREEN,
        Color.GREEN
    )

    var myList = ColorStateList(states, colors)

    constructor(context: Context) : super(context) {
        appAttrs = context.theme.obtainStyledAttributes(null, R.styleable.MyTextInputLayout, 0, 0)
        normal =
            appAttrs.getDrawable(R.styleable.MyTextInputLayout_textInputLayoutNormalSelectorDrawable)
        error = appAttrs.getDrawable(R.styleable.MyTextInputLayout_textInputLayoutErrorDrawable)
        editText?.background  = normal
//        setHintTextAppearance(R.style.HintTextAppearance)
//        counterOverflowTextColor = myList
//        counterTextColor = myList
//        defaultHintTextColor = myList
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        appAttrs = context.theme.obtainStyledAttributes(attrs, R.styleable.MyTextInputLayout, 0, 0)
        normal =
            appAttrs.getDrawable(R.styleable.MyTextInputLayout_textInputLayoutNormalSelectorDrawable)
        error = appAttrs.getDrawable(R.styleable.MyTextInputLayout_textInputLayoutErrorDrawable)
        editText?.background  = normal
//        setHintTextAppearance(R.style.HintTextAppearance)
//        counterOverflowTextColor = myList
//        counterTextColor = myList
//        defaultHintTextColor = myList
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        appAttrs = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MyTextInputLayout,
            defStyleAttr,
            0
        )
        normal =
            appAttrs.getDrawable(R.styleable.MyTextInputLayout_textInputLayoutNormalSelectorDrawable)
        error = appAttrs.getDrawable(R.styleable.MyTextInputLayout_textInputLayoutErrorDrawable)
        editText?.background  = normal
//        setHintTextAppearance(R.style.HintTextAppearance)
//        counterOverflowTextColor = myList
//        counterTextColor = myList
//        defaultHintTextColor = myList
    }

//    override fun onViewAdded(child: View?) {
//        super.onViewAdded(child)
//        if(child is EditText) {
//            child.setOnFocusChangeListener { view, b ->
//                isEndIconVisible = b
//                Log.d("TAG", "setOnFocusChangeListener")
//            }
//        }
//    }

    override fun setOnFocusChangeListener(l: OnFocusChangeListener?) {
        super.setOnFocusChangeListener(l)
        Log.d("TAG", "setOnFocusChangeListener")
    }

    override fun getOnFocusChangeListener(): OnFocusChangeListener {
        Log.d("TAG", "getOnFocusChangeListener")
        return super.getOnFocusChangeListener()
    }

    override fun setErrorEnabled(enabled: Boolean) {
        super.setErrorEnabled(enabled)
        editText?.background = if (enabled) {
            error
        } else {
            normal
        }
//        counterOverflowTextColor = myList
//        counterTextColor = myList
//        setErrorTextColor(myList)
//        try {
//            rootView.parent
//            val textInputError: AppCompatTextView = rootView.findViewById(R.id.textinput_error)
//            Log.d("TAG", textInputError.tag.toString())
//        } catch (ex: Exception) {
//            Log.d("TAG", ex.message.toString())
//        }
//        setHintTextAppearance(R.style.HintTextAppearance)
//        placeholderTextColor = myList
//        hintTextColor = myList
//        defaultHintTextColor = myList
    }
}