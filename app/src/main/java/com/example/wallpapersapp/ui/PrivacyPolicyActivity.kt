package com.example.wallpapersapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.wallpapersapp.R
import com.example.wallpapersapp.ads.AdsManager
import com.example.wallpapersapp.databinding.ActivityPrivacyPolicyBinding
import com.example.wallpapersapp.ui.MainActivity
import com.example.wallpapersapp.utils.PreferenceManager


class PrivacyPolicyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrivacyPolicyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_privacy_policy)

        val adsManager = AdsManager(this)

//        binding.privacyText.text= Html.fromHtml("By proceeding you agree to <u>Privacy Policy</u>.")

        binding.privacyText.colorSpannableStringWithUnderLineOne(
            "By proceeding you agree to ",
            "Privacy Policy.",
            callback = {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://sites.google.com/view/zptech/home")
                )
                startActivity(browserIntent)
            })
        binding.privacyText.invalidate()

        binding.btnStart.setOnClickListener {
            if (binding.privacyText.isChecked) {
                PreferenceManager(this).privacyPolicy = true
                startActivity(Intent(this@PrivacyPolicyActivity, MainActivity::class.java))
                adsManager.showInterstitialSplash()
               this@PrivacyPolicyActivity.finish()
            } else {
                Toast.makeText(
                    this,
                    "Please accept Privacy policy to Continue using app.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        adsManager.loadBanner(binding.rel)
        adsManager.loadInterstitialSplash()
    }

    private fun TextView.colorSpannableStringWithUnderLineOne(
        prefixString: String,
        postfixString: String,
        callback: (Int) -> Unit
    ) {
        val spanTxt = SpannableStringBuilder()
        spanTxt.append("$prefixString ")
        spanTxt.append(" $postfixString")
        spanTxt.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                callback(0)
                widget.invalidate()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = ContextCompat.getColor(context, R.color.white)
                ds.isUnderlineText = true
            }
        }, prefixString.length, spanTxt.length, 0)
        this.movementMethod = LinkMovementMethod.getInstance()
        this.setText(spanTxt, TextView.BufferType.SPANNABLE)
    }
}