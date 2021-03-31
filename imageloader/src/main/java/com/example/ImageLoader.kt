package com.example

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.ImageView
import com.example.ImageDownloader.Companion.IMAGE_DOWNLOAD_COMPLETE
import com.example.model.DownloadResult
import com.example.network.ImageViewAction
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object ImageLoader {
    private var placeHolder: Int = 0
    private var imageUrl: String = ""
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

    fun load(imageUrl: String): ImageLoader {
        this.imageUrl = imageUrl
        return this
    }

    fun into(target: ImageView) {
        // TODO: check memory cache
        // TODO: check disk cache
        service.submit(ImageViewAction(target, imageUrl, downloader))
        // TODO: save bitmap to cache
    }
}