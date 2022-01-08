package com.example.wallpapersapp.widgets

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CustomBoldTextView(context: Context, attrs: AttributeSet?) :
    AppCompatTextView(context, attrs) {

    init {

        setFont()
    }

    private fun setFont() {
        val font = Typeface.createFromAsset(context.assets, "Roboto-Bold.ttf")
        typeface = font
    }
}