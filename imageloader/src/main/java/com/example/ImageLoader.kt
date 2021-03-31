package com.example

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.example.ImageDownloader.Companion.IMAGE_DOWNLOAD_COMPLETE
import com.example.model.DownloadResult
import com.example.network.ImageViewAction
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ImageLoader {
    private val service: ExecutorService = Executors.newCachedThreadPool()
    private val mainThreadHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                IMAGE_DOWNLOAD_COMPLETE -> {
                    (msg.obj as DownloadResult).let {
                        it.target.setImageBitmap(it.bitmap)
                    }
                }
            }
        }
    }
    private var downloader = ImageDownloader(mainThreadHandler)

    fun load(imageUrl: String): ActionCreator {
        return ActionCreator(this, imageUrl, downloader)
    }

    fun submit(action: ImageViewAction) {
        service.submit(action)
    }

    companion object {
        val instance by lazy {
            ImageLoader()
        }
    }
}