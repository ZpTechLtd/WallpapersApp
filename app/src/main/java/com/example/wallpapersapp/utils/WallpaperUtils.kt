package com.example.wallpapersapp.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.wallpapersapp.BuildConfig
import com.example.wallpapersapp.model.Result
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL


private val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
private val parent: File = File("${path}/Wallpapers/")

fun Context.download(
    result: Result,
    rlProgress: View,
    root: View,
    lnbottom: View,
    isApply: Boolean, isShare: Boolean
) {
    CoroutineScope(Dispatchers.IO).launch {

        try {
            if (!parent.exists()) {
                parent.mkdirs()
            }
            val file = File(parent.absolutePath, "${result?.objectId}.jpg")
            val url = URL(result?.imageUrl?.url)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            val myBitmap = BitmapFactory.decodeStream(input)
            val out = FileOutputStream(file)
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.close()

            message(true, "", rlProgress, root, lnbottom)

            if (isApply) {
                setWallpapers(file.absolutePath)
            }

            if (isShare) {
                share(file.absolutePath)
            }


        } catch (exp: Exception) {
            exp.message?.let {
                if (it.toLowerCase().contains("unable to resolve host")) {
                    message(
                        false,
                        "Make sure your internet connection is working",
                        rlProgress,
                        root,
                        lnbottom
                    )
                } else {
                    message(false, it, rlProgress, root, lnbottom)
                }
            }


        }

    }


}

fun Context.message(
    succes: Boolean,
    message: String,
    rlProgress: View,
    root: View,
    lnbottom: View
) {
    CoroutineScope(Dispatchers.Main)
        .launch {


            rlProgress.visibility = View.GONE
            if (succes) {
                val snackbar = Snackbar
                    .make(root, "Save Successfully", Snackbar.LENGTH_LONG)
                snackbar.anchorView = lnbottom
                snackbar.show()
            } else {
                Toast.makeText(this@message, "$message", Toast.LENGTH_SHORT).show()
            }
        }
}


fun isImageExists(result: Result): Boolean {

    if (!parent.exists()) {
        parent.mkdirs()
    }

    val file = File(parent.absolutePath + "/${result?.objectId}.jpg")
    return file.exists()
}

fun getImagePath(result: Result): String {

    if (!parent.exists()) {
        parent.mkdirs()
    }

    var file = File(parent.absolutePath + "/${result?.objectId}.jpg")
    return file.absolutePath
}


fun Context.setWallpapers(path: String) {

    try {
        val intent = Intent(Intent.ACTION_ATTACH_DATA)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(Uri.parse(path), "image/jpg")
        intent.putExtra("mimeType", "image/jpg")
        this.startActivity(Intent.createChooser(intent, "Set as:"))
    } catch (exp: Exception) {

        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(this@setWallpapers, "${exp.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

fun Context.share(path: String) {
    try {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/*"
        val file = File(path)
        val uri: Uri = FileProvider.getUriForFile(
            this,
            BuildConfig.APPLICATION_ID.toString() + ".fileprovider",
            file
        )
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(intent)
    } catch (exception: IllegalStateException) {
        Toast.makeText(this, "Exception sharing...", Toast.LENGTH_SHORT).show()
    } catch (exception: IllegalArgumentException) {
        Toast.makeText(this, "Exception sharing...", Toast.LENGTH_SHORT).show()
    } catch (exception: ActivityNotFoundException) {
        Toast.makeText(this, "Exception sharing...", Toast.LENGTH_SHORT).show()
    } catch (exception: NullPointerException) {
        Toast.makeText(this, "Exception sharing...", Toast.LENGTH_SHORT).show()
    }
}