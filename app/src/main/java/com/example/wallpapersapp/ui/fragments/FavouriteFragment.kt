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
import com.example.wallpapersapp.ui.ImageActivity
import com.example.wallpapersapp.OnItemClick
import com.example.wallpapersapp.R
import com.example.wallpapersapp.adapter.WallpapersAdapter
import com.example.wallpapersapp.databinding.FragmentFavouriteBinding
import com.example.wallpapersapp.model.Result
import com.example.wallpapersapp.ui.MainActivity
import com.example.wallpapersapp.utils.GridSpacingItemDecoration
import com.example.wallpapersapp.viewmodel.SharedViewModel


class FavouriteFragment constructor() : Fragment(), OnItemClick {

    private lateinit var binding: FragmentFavouriteBinding
    private lateinit var viewModel: SharedViewModel
    private var adapter: WallpapersAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite, container, false)
        viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager = GridLayoutManager(activity, 2)
        binding.recyclerview.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                resources.getDimensionPixelOffset(R.dimen._10sdp),
                true
            )
        )
        adapter = WallpapersAdapter(mutableListOf(), this)
        binding.recyclerview.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeFav().observe(viewLifecycleOwner, Observer {
            binding.txtError.visibility = View.GONE
            adapter?.setData(it)
            if (it.isNullOrEmpty()) {
                binding.txtError.visibility = View.VISIBLE
            }

        })
    }

    override fun onClick(result: Result, view: View) {

        val intent = Intent(activity, ImageActivity::class.java)
        intent.putExtra("result", result)
        startActivity(Intent(intent))
        MainActivity.adManager?.showInterstitial()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavList()
    }
}