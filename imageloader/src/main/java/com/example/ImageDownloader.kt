package com.example

import android.graphics.BitmapFactory
import android.os.Handler
import android.widget.ImageView
import com.example.ImageLoader.Companion.diskCache
import com.example.model.DownloadResult
import com.example.network.ImageDownloadService
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class ImageDownloader(private val mainThreadHandler: Handler) {
    private val client by lazy {
        OkHttpClient.Builder().apply {
            cache(Cache(diskCache, calculateDiskCacheSize(diskCache)))
        }.build()
    }
    private val api =
        Retrofit.Builder().client(client).baseUrl(BASE_URL).build()
            .create(ImageDownloadService::class.java)

    fun downloadImage(target: ImageView, imageUrl: String) {
        val apiResult = api.downloadImage(imageUrl).execute()
        if (apiResult.isSuccessful) {
            apiResult.body()?.let {
                val bitmap = BitmapFactory.decodeStream(it.byteStream())
                val result = DownloadResult(bitmap, null, target)

                if (ImageLoader.loadCache(imageUrl) != bitmap) {
                    ImageLoader.saveCache(imageUrl, bitmap)
                }

                mainThreadHandler.run {
                    sendMessage(obtainMessage(IMAGE_DOWNLOAD_COMPLETE, result))
                }
            }
            return
        }
        val result = DownloadResult(null, apiResult.message(), target)
        mainThreadHandler.run {
            sendMessage(obtainMessage(IMAGE_DOWNLOAD_FAIL, result))
        }
    }

    companion object {
        const val BASE_URL = "https://developers.kakao.com/"

        const val IMAGE_DOWNLOAD_COMPLETE = 1
        const val IMAGE_DOWNLOAD_FAIL = 2
    }
}