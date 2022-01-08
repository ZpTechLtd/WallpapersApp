package com.example.wallpapersapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.wallpapersapp.OnItemClick
import com.example.wallpapersapp.R
import com.example.wallpapersapp.databinding.ItemWallpapersBinding
import com.example.wallpapersapp.model.Result
import java.lang.NullPointerException

class WallpapersAdapter(var list: List<Result>, val onItemClick: OnItemClick) :
    RecyclerView.Adapter<WallpapersAdapter.Holder>() {


    class Holder(itemView: ItemWallpapersBinding) : RecyclerView.ViewHolder(itemView.root) {
        val binding = itemView
        fun bind(result: Result) {

            Glide.with(binding.image.context).load(result.imageUrl.url)
                .placeholder(R.drawable.placeholder).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.image)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_wallpapers, parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position])

        holder.binding.root.setOnClickListener {
            onItemClick.onClick(list[position], holder.binding.root)
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    fun setData(it: List<Result>?) {
        try {
            list= it!!
            notifyDataSetChanged()
        }
        catch (exp:NullPointerException){}

    }
}