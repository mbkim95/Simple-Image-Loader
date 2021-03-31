package com.example

import android.graphics.BitmapFactory
import android.os.Handler
import android.widget.ImageView
import com.example.model.ImageViewAction
import com.example.network.ImageDownloadService
import retrofit2.Retrofit

class ImageDownloader(private val mainThreadHandler: Handler) {
    private val api =
        Retrofit.Builder().baseUrl(BASE_URL).build().create(ImageDownloadService::class.java)

    fun downloadImage(target: ImageView, imageUrl: String) {
        val result = api.downloadImage(imageUrl).execute()
        if (result.isSuccessful) {
            result.body()?.let {
                val bitmap = BitmapFactory.decodeStream(it.byteStream())
                val action = ImageViewAction(bitmap, null, target)

                mainThreadHandler.run {
                    sendMessage(obtainMessage(IMAGE_DOWNLOAD_COMPLETE, action))
                }
            }
            return
        }
        val action = ImageViewAction(null, result.message(), target)
        mainThreadHandler.run {
            sendMessage(obtainMessage(IMAGE_DOWNLOAD_FAIL, action))
        }
    }

    companion object {
        const val BASE_URL = "https://developers.kakao.com/"

        const val IMAGE_DOWNLOAD_COMPLETE = 1
        const val IMAGE_DOWNLOAD_FAIL = 2
    }
}