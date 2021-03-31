package com.example

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.LruCache
import android.widget.ImageView
import com.example.ImageDownloader.Companion.IMAGE_DOWNLOAD_COMPLETE
import com.example.model.DownloadResult
import com.example.network.ImageViewAction
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ImageLoader {
    private val service: ExecutorService = Executors.newCachedThreadPool()
    private var downloader = ImageDownloader(mainThreadHandler)

    fun load(imageUrl: String): ActionCreator {
        return ActionCreator(this, imageUrl, downloader)
    }

    fun submit(action: ImageViewAction) {
        service.submit(action)
    }

    companion object {
        private lateinit var cache: LruCache<String, Bitmap>
        private val instance by lazy {
            ImageLoader()
        }

        @JvmStatic
        fun get(context: Context): ImageLoader {
            if (!::cache.isInitialized) {
                cache = LruCache(calculateMemoryCacheSize(context))
            }
            return instance
        }

        @JvmStatic
        internal fun loadCache(imageUrl: String): Bitmap? = cache.get(imageUrl)

        @JvmStatic
        internal fun saveCache(imageUrl: String, bitmap: Bitmap) = cache.put(imageUrl, bitmap)

        private val mainThreadHandler: Handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    IMAGE_DOWNLOAD_COMPLETE -> {
                        (msg.obj as DownloadResult).let {
                            drawBitmap(it.target, it.bitmap)
                        }
                    }
                }
            }
        }

        @JvmStatic
        fun drawBitmap(target: ImageView, bitmap: Bitmap?) {
            bitmap?.let {
                target.setImageBitmap(bitmap)
            }
        }
    }
}