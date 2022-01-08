package com.example.wallpapersapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wallpapersapp.BaseApplication
import com.example.wallpapersapp.R
import com.example.wallpapersapp.Resource
import com.example.wallpapersapp.RetrofitClient
import com.example.wallpapersapp.model.Result
import com.example.wallpapersapp.model.WallpaperResponse
import com.example.wallpapersapp.utils.PreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    val app = application

    private var wallpapersList = MutableLiveData<Resource<*>>()
    private var preference = PreferenceManager(application)
    private var favList = MutableLiveData<List<Result>>()

    fun observerWallpapersList(): LiveData<Resource<*>> {
        return wallpapersList
    }

    fun getFavList(){
        favList.postValue(preference.list)
    }

    fun observeFav(): LiveData<List<Result>> {
        return favList
    }


    fun call() {

        val call: Call<WallpaperResponse> = RetrofitClient.service.getWallpapers(
            app.resources.getString(R.string.appId),
            app.getString(R.string.appKey)
        )

        call.enqueue(object : Callback<WallpaperResponse> {
            override fun onResponse(
                call: Call<WallpaperResponse>,
                response: Response<WallpaperResponse>
            ) {
                if (response.isSuccessful) {

                    wallpapersList.postValue(Resource.success(response.body()))

                } else {
                    wallpapersList.postValue(Resource.error(null, response.message()))
                }
            }

            override fun onFailure(call: Call<WallpaperResponse>, t: Throwable) {
                wallpapersList.postValue(
                    Resource.error(
                        null,
                        "Make sure your internet connection is working properly."
                    )
                )
            }

        })
    }
}