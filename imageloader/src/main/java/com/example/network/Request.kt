package com.example.network

import android.widget.ImageView
import com.example.ImageDownloader

class Request(
    private val target: ImageView,
    private val imageUrl: String,
    private val downloader: ImageDownloader
) : Runnable {
    override fun run() {
        downloader.downloadImage(target, imageUrl)
    }
}