package com.example.betssonrequest

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputLayout

class MyTextInputLayout : TextInputLayout {
    private val normal: Drawable?
    private val error: Drawable?
    private val appAttrs: TypedArray

    constructor(context: Context) : super(context) {
        appAttrs = context.theme.obtainStyledAttributes(null, R.styleable.MyTextInputLayout, 0, 0)
        normal =
            appAttrs.getDrawable(R.styleable.MyTextInputLayout_textInputLayoutNormalSelectorDrawable)
        error = appAttrs.getDrawable(R.styleable.MyTextInputLayout_textInputLayoutErrorDrawable)
        editText?.background = normal
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        appAttrs = context.theme.obtainStyledAttributes(attrs, R.styleable.MyTextInputLayout, 0, 0)
        normal =
            appAttrs.getDrawable(R.styleable.MyTextInputLayout_textInputLayoutNormalSelectorDrawable)
        error = appAttrs.getDrawable(R.styleable.MyTextInputLayout_textInputLayoutErrorDrawable)
        editText?.background  = normal
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
    }

    override fun setErrorEnabled(enabled: Boolean) {
        super.setErrorEnabled(enabled)
        editText?.background = if (enabled) {
            error
        } else {
            normal
        }
    }
}