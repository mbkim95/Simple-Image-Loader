package com.example.network

import android.widget.ImageView
import com.example.ImageDownloader

class ImageViewAction(
    private val target: ImageView,
    private val imageUrl: String,
    private val downloader: ImageDownloader
) : Runnable {
    override fun run() {
        downloader.downloadImage(target, imageUrl)
    }
}