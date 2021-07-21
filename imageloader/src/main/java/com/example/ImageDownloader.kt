package com.example

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.widget.ImageView
import com.example.ImageLoader.Companion.diskCache
import com.example.model.DecodingResult
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
        Retrofit.Builder().client(client).baseUrl(Constants.BASE_URL).build()
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
                    sendMessage(obtainMessage(Constants.IMAGE_DOWNLOAD_COMPLETE, result))
                }
            }
            return
        }
        val result = DownloadResult(null, apiResult.message(), target)
        mainThreadHandler.run {
            sendMessage(obtainMessage(Constants.IMAGE_DOWNLOAD_FAIL, result))
        }
    }

    fun downloadImage(imageUrl: String, callback: (bitmap: Bitmap?) -> Unit) {
        val apiResult = api.downloadImage(imageUrl).execute()
        val result: DecodingResult

        if (apiResult.isSuccessful) {
            apiResult.body()?.let {
                val bitmap = BitmapFactory.decodeStream(it.byteStream())
                result = DecodingResult(bitmap, null, callback)

                if (ImageLoader.loadCache(imageUrl) != bitmap) {
                    ImageLoader.saveCache(imageUrl, bitmap)
                }

                mainThreadHandler.run {
                    sendMessage(obtainMessage(Constants.IMAGE_DOWNLOAD_COMPLETE, result))
                }
            }
            return
        }
        result = DecodingResult(null, apiResult.message(), callback)
        mainThreadHandler.run {
            sendMessage(obtainMessage(Constants.IMAGE_DOWNLOAD_FAIL, result))
        }
    }
}
