package com.example.betssonrequest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

public class CustomTextInputLayout extends TextInputLayout {

    private final Drawable normal;
    private final Drawable error;
    private final TypedArray appAttrs;

    public CustomTextInputLayout(@NonNull @NotNull Context context) {
        super(context);
        appAttrs = context.getTheme().obtainStyledAttributes(null, R.styleable.MyTextInputLayout, 0, 0);
        normal = appAttrs.getDrawable(R.styleable.MyTextInputLayout_textInputLayoutNormalSelectorDrawable);
        error = appAttrs.getDrawable(R.styleable.MyTextInputLayout_textInputLayoutErrorDrawable);
        setBackground(normal);
    }

    public CustomTextInputLayout(@NonNull @NotNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        appAttrs = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyTextInputLayout, 0, 0);
        normal = appAttrs.getDrawable(R.styleable.MyTextInputLayout_textInputLayoutNormalSelectorDrawable);
        error = appAttrs.getDrawable(R.styleable.MyTextInputLayout_textInputLayoutErrorDrawable);
        setBackground(normal);
    }

    public CustomTextInputLayout(@NonNull @NotNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        appAttrs = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyTextInputLayout, defStyleAttr, 0);
        normal = appAttrs.getDrawable(R.styleable.MyTextInputLayout_textInputLayoutNormalSelectorDrawable);
        error = appAttrs.getDrawable(R.styleable.MyTextInputLayout_textInputLayoutErrorDrawable);
        setBackground(normal);
    }

    @Override
    public void setErrorEnabled(boolean enabled) {
        super.setErrorEnabled(enabled);
        if(enabled) {
            setBackground(error);
        } else {
            setBackground(normal);
        }
    }
}
