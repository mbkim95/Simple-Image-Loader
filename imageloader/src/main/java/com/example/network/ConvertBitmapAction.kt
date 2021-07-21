package com.example.network

import android.graphics.Bitmap
import com.example.ImageDownloader

class ConvertBitmapAction(
    private val imageUrl: String,
    private val downloader: ImageDownloader,
    private val callback: (bitmap: Bitmap?) -> Unit
) : Runnable {
    override fun run() {
        downloader.downloadImage(imageUrl, callback)
    }
}