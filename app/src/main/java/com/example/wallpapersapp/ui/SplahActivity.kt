package com.example.wallpapersapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.wallpapersapp.R
import com.example.wallpapersapp.ads.AdsManager
import com.example.wallpapersapp.utils.PreferenceManager
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

private const val TAG="SplahActivity"
class SplahActivity : AppCompatActivity() {
    private var handlerConfiguration: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splah)
        val adManager = AdsManager(this)
        val preferenceManager = PreferenceManager(this@SplahActivity)

        val icon: ImageView = findViewById(R.id.icon)
        val adContainer: FrameLayout = findViewById(R.id.adContainer)
        val title: TextView = findViewById(R.id.title)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide)
        icon.startAnimation(slideAnimation)
        title.startAnimation(slideAnimation)
        adManager.loadNativeLarge(adContainer)
        adManager.loadInterstitialSplash()

        val remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }

        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetch(0)
            .addOnSuccessListener {
                remoteConfig.fetchAndActivate()
                handlerConfiguration = Handler(Looper.getMainLooper())
                handlerConfiguration!!.postDelayed(Runnable {
                    if (!TextUtils.isEmpty(remoteConfig.getString("FF_Wallpaper_INterstitial"))) {
                        preferenceManager.admobInterstitial =
                            remoteConfig.getString("FF_Wallpaper_INterstitial")
                    }

                    if (!TextUtils.isEmpty(remoteConfig.getString("FF_Wallpaper_App_Open"))) {
                        preferenceManager.admobOpen =
                            remoteConfig.getString("FF_Wallpaper_App_Open")
                    }


                    if (!TextUtils.isEmpty(remoteConfig.getString("FF_Wallpaper_Native"))) {
                        preferenceManager.admoB_native =
                            remoteConfig.getString("FF_Wallpaper_Native")
                    }

                    if (!TextUtils.isEmpty(remoteConfig.getString("FF_Wallpapers_Banner"))) {
                        preferenceManager.admoB_native_banner =
                            remoteConfig.getString("FF_Wallpapers_Banner")
                    }
                }, 3000)

            }

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            adManager.showInterstitialSplash()
            finish()
        }, 8000)
    }
}