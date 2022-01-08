package com.example.wallpapersapp

import android.view.View
import com.example.wallpapersapp.model.Result

interface OnItemClick {

    fun onClick(result:Result,view:View)
}