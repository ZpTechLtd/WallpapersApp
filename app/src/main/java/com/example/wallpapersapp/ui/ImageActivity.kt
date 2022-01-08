package com.example.wallpapersapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.wallpapersapp.R
import com.example.wallpapersapp.databinding.FragmentImageBinding
import com.example.wallpapersapp.model.Result
import com.example.wallpapersapp.pinchtozoom.ImageMatrixTouchHandler
import com.example.wallpapersapp.utils.*

class ImageActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: FragmentImageBinding
    private var bundle: Intent? = null
    private var result: Result? = null
    private var preferenceManager: PreferenceManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_image)
        preferenceManager = PreferenceManager(this)



        bundle = intent
        result = bundle?.getSerializableExtra("result") as Result
        binding.isFavourite = preferenceManager?.isFavourite(result)
        binding.imgFavourite.setOnClickListener(this)
        binding.imgBack.setOnClickListener(this)
        binding.lnDownload.setOnClickListener(this)
        binding.lnApply.setOnClickListener(this)
        binding.lnShare.setOnClickListener(this)
        binding.imgWall.setOnTouchListener(ImageMatrixTouchHandler(this))

        Glide.with(this).load(result?.imageUrl?.url).into(binding.imgWall)


    }

    override fun onClick(v: View?) {


        when (v?.id) {
            R.id.imgBack -> {
                finish()
                MainActivity.adManager?.showInterstitial()
            }

            R.id.imgFavourite -> {
                binding.isFavourite = !preferenceManager!!.isFavourite(result)
                preferenceManager?.save(result)

            }

            R.id.lnDownload -> {
                if (!isPermissionGranted()) {
                    requestPermission()
                    return
                }
                if (!isImageExists(result!!)) {
                    binding.rlProgress.visibility = View.VISIBLE
                    download(
                        result!!,
                        binding.rlProgress,
                        binding.root,
                        binding.lnbottom,
                        false, false
                    )
                } else {
                    Toast.makeText(this, "Already Downloaded..", Toast.LENGTH_SHORT).show()
                }
            }

            R.id.lnApply -> {
                if (!isPermissionGranted()) {
                    requestPermission()
                    return
                }
                if (!isImageExists(result!!)) {
                    binding.rlProgress.visibility = View.VISIBLE
                    download(
                        result!!,
                        binding.rlProgress,
                        binding.root,
                        binding.lnbottom,
                        true,
                        false
                    )
                } else {
                    setWallpapers(getImagePath(result!!))
                }
            }

            R.id.lnShare -> {
                if (!isPermissionGranted()) {
                    requestPermission()
                    return
                }
                if (!isImageExists(result!!)) {
                    binding.rlProgress.visibility = View.VISIBLE
                    download(
                        result!!,
                        binding.rlProgress,
                        binding.root,
                        binding.lnbottom,
                        false,
                        true
                    )
                } else {
                    share(getImagePath(result!!))
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        MainActivity.adManager?.showInterstitial()
    }
}