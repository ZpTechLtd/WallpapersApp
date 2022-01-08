package com.example.wallpapersapp.widgets

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CustomRegularTextView(context: Context, attrs: AttributeSet?) :
    AppCompatTextView(context, attrs) {

    init {

        val font = Typeface.createFromAsset(context.assets, "Roboto-Regular.ttf")
        typeface = font
    }

    init {

        setFont()
    }

    private fun setFont() {
        val font = Typeface.createFromAsset(context.assets, "Roboto-Regular.ttf")
        typeface = font
    }
}