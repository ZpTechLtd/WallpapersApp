package com.example.wallpapersapp

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.example.wallpapersapp.model.WallpaperResponse
import okhttp3.*
import okhttp3.CacheControl
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import java.io.File
import java.util.concurrent.TimeUnit


object RetrofitClient {
    const val HEADER_CACHE_CONTROL = "Cache-Control"
    const val HEADER_PRAGMA = "Pragma"

    var mCache: Cache? = null


    var httpClient = OkHttpClient.Builder()
        .addInterceptor(provideOfflineCacheInterceptor())
        .addNetworkInterceptor(provideCacheInterceptor())
        .cache(provideCache())


    val service = Retrofit.Builder()
        .baseUrl("https://parseapi.back4app.com/classes/")
        .client(httpClient.build())
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(GetService::class.java)


    private fun provideCache(): Cache? {
        if (mCache == null) {
            try {
                mCache = Cache(
                    File(BaseApplication.context?.getCacheDir(), "http-cache"),
                    10 * 1024 * 1024
                ) // 10 MB
            } catch (e: Exception) {
            }
        }
        return mCache
    }

    private fun provideCacheInterceptor(): Interceptor? {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())
            val cacheControl: CacheControl
            cacheControl = if (isConnected()) {
                CacheControl.Builder()
                    .maxAge(10, TimeUnit.DAYS)
                    .build()
            } else {
                CacheControl.Builder()
                    .maxStale(10, TimeUnit.DAYS)
                    .build()
            }
            response.newBuilder()
                .removeHeader(HEADER_PRAGMA)
                .removeHeader(HEADER_CACHE_CONTROL)
                .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                .build()
        }
    }

    private fun provideOfflineCacheInterceptor(): Interceptor? {
        return Interceptor { chain ->
            var request = chain.request()
            if (!isConnected()) {
                val cacheControl = CacheControl.Builder()
                    .maxStale(10, TimeUnit.DAYS)
                    .build()
                request = request.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .cacheControl(cacheControl)
                    .build()
            }
            chain.proceed(request)
        }
    }

    private fun isConnected(): Boolean {
        try {
            val e = BaseApplication.context?.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            val activeNetwork = e.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        } catch (e: Exception) {
        }
        return false
    }
}

interface GetService {

    @GET("Wallpapers")
    fun getWallpapers(
        @Header("X-Parse-Application-Id") applicationId: String,
        @Header("X-Parse-REST-API-Key") restApitKey: String,
    ): Call<WallpaperResponse>
}







