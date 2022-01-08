package com.example.wallpapersapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wallpapersapp.BuildConfig
import com.example.wallpapersapp.ui.ImageActivity
import com.example.wallpapersapp.OnItemClick
import com.example.wallpapersapp.R
import com.example.wallpapersapp.Status
import com.example.wallpapersapp.adapter.WallpapersAdapter
import com.example.wallpapersapp.databinding.FragmentHomeBinding
import com.example.wallpapersapp.model.Result
import com.example.wallpapersapp.model.WallpaperResponse
import com.example.wallpapersapp.ui.MainActivity
import com.example.wallpapersapp.utils.GridSpacingItemDecoration
import com.example.wallpapersapp.viewmodel.SharedViewModel


class HomeFragment constructor() : Fragment(), OnItemClick {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        binding.shimmerVisbility = true
        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager = GridLayoutManager(activity, 2)
        binding.recyclerview.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                resources.getDimensionPixelOffset(R.dimen._10sdp),
                true
            )
        )

        binding.share.setOnClickListener {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
                var shareMessage = "FFF Wallpapers App\n\n"
                shareMessage =
                    """
                    ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                    
                    
                    """.trimIndent()
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage.trim())
                startActivity(Intent.createChooser(shareIntent, "Share"))
            } catch (e: Exception) {
                //e.toString();
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.call()

        viewModel.observerWallpapersList().observe(viewLifecycleOwner, Observer {
            binding.shimmerVisbility = false
            binding.txtError.visibility = View.GONE
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data as WallpaperResponse
                    if (!data.results.isNullOrEmpty()) {
                        val list = data.results
                        list.reverse()
                        binding.recyclerview.adapter = WallpapersAdapter(list, this)
                    }
                }
                Status.ERROR -> {
                    binding.txtError.visibility = View.VISIBLE
                    binding.txtError.text = it.message
                }

            }
        })
    }

    override fun onClick(result: Result, view: View) {

        val intent = Intent(activity, ImageActivity::class.java)
        intent.putExtra("result", result)
        startActivity(Intent(intent))
        MainActivity.adManager?.showInterstitial()
    }
}