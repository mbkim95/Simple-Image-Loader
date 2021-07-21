package com.example

import android.os.Looper
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.ImageLoader.Companion.drawBitmap
import com.example.network.ImageViewAction

class ActionCreator(
    private val loader: ImageLoader,
    private val imageUrl: String?,
    private val downloader: ImageDownloader
) {
    private var placeHolder: Int = Constants.NO_PLACEHOLDER

    fun placeHolder(resourceId: Int): ActionCreator {
        this.placeHolder = resourceId
        return this
    }

    fun into(target: ImageView) {
        if (Looper.getMainLooper().thread != Thread.currentThread()) {
            throw IllegalStateException("Method call should happen from the main thread.")
        }

        if (placeHolder != Constants.NO_PLACEHOLDER) {
            target.setImageDrawable(ContextCompat.getDrawable(target.context, placeHolder))
        }
        if (imageUrl == null) {
            Log.e(Constants.TAG, "imageUrl is null")
            return
        }

        val bitmap = ImageLoader.loadCache(imageUrl)
        bitmap?.let {
            drawBitmap(target, it)
            return
        }
        loader.submit(ImageViewAction(target, imageUrl, downloader))
    }
}