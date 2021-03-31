package com.example

import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.network.ImageViewAction

class ActionCreator(
    private val loader: ImageLoader,
    private val imageUrl: String,
    private val downloader: ImageDownloader
) {
    private var placeHolder: Int = 0

    fun placeHolder(resourceId: Int): ActionCreator {
        this.placeHolder = resourceId
        return this
    }

    fun into(target: ImageView) {
        if (placeHolder != 0) {
            target.setImageDrawable(ContextCompat.getDrawable(target.context, placeHolder))
        }
        // TODO: check memory cache
        // TODO: check disk cache
        loader.submit(ImageViewAction(target, imageUrl, downloader))
        // TODO: save bitmap to cache
    }
}