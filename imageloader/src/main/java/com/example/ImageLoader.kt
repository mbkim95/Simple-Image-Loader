package com.example

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.util.LruCache
import android.widget.ImageView
import com.example.model.DecodingResult
import com.example.model.DownloadResult
import com.example.network.ConvertBitmapAction
import com.example.network.ImageViewAction
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ImageLoader {
    private val service: ExecutorService = Executors.newCachedThreadPool()
    private var downloader = ImageDownloader(mainThreadHandler)

    fun load(imageUrl: String?): ActionCreator {
        return ActionCreator(this, imageUrl, downloader)
    }

    fun submit(action: ImageViewAction) {
        service.submit(action)
    }

    fun submit(action: ConvertBitmapAction) {
        service.submit(action)
    }

    companion object {
        internal lateinit var diskCache: File
        private lateinit var memoryCache: LruCache<String, Bitmap>
        private const val DISK_CACHE = "image-loader-cache"

        private val instance by lazy {
            ImageLoader()
        }

        @JvmStatic
        fun get(context: Context): ImageLoader {
            if (!::memoryCache.isInitialized) {
                memoryCache = LruCache(calculateMemoryCacheSize(context))
            }
            if (!::diskCache.isInitialized) {
                diskCache = File(context.applicationContext.cacheDir, DISK_CACHE)
                if (!diskCache.exists()) {
                    diskCache.mkdirs()
                }
            }
            return instance
        }

        @JvmStatic
        internal fun loadCache(imageUrl: String): Bitmap? = memoryCache.get(imageUrl)

        @JvmStatic
        internal fun saveCache(imageUrl: String, bitmap: Bitmap) = memoryCache.put(imageUrl, bitmap)

        private val mainThreadHandler: Handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    Constants.IMAGE_DOWNLOAD_COMPLETE -> {
                        when (val result = msg.obj) {
                            is DownloadResult -> {
                                drawBitmap(result.target, result.bitmap)
                            }
                            is DecodingResult -> {
                                getBitmap(result.bitmap, result.callback)
                            }
                        }
                    }
                    Constants.IMAGE_DOWNLOAD_FAIL -> {
                        when (val result = msg.obj) {
                            is DownloadResult -> {
                                Log.e(Constants.TAG, "Image Loading failed: ${result.error}")
                            }
                            is DecodingResult -> {
                                getBitmap(null, result.callback)
                            }
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

        @JvmStatic
        fun getBitmap(bitmap: Bitmap?, callback: (bitmap: Bitmap?) -> Unit) {
            callback(bitmap)
        }
    }
}