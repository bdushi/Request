package com.example.betssonrequest

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout


class MyTextInputLayout : TextInputLayout {
    private var normal: Drawable? = null
    private var error: Drawable? = null
    constructor(context: Context) : super(context) {
        init(context = context, attrs = null, defStyleAttr = 0)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context = context, attrs = attrs, defStyleAttr = 0)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context = context, attrs = attrs, defStyleAttr = defStyleAttr)
    }
    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val appAttrs = context.theme.obtainStyledAttributes(attrs, R.styleable.MyTextInputLayout, defStyleAttr, 0)
        normal = appAttrs.getDrawable(R.styleable.MyTextInputLayout_textInputLayoutNormalSelectorDrawable)
        error = appAttrs.getDrawable(R.styleable.MyTextInputLayout_textInputLayoutErrorDrawable)
        background = normal
//        background = appAttrs.getDrawable(R.styleable.MyTextInputLayout_textInputLayoutNormalSelectorDrawable)
//        background = ContextCompat.getDrawable(context, R.drawable.text_input_white_background)
        // Recycle attrs
        appAttrs.recycle()
    }

    override fun setErrorEnabled(enabled: Boolean) {
        background = if(enabled) {
            error
        } else {
            normal
        }
    }
}