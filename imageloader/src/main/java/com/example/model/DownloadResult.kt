package com.example.model

import android.graphics.Bitmap
import android.widget.ImageView

data class DownloadResult(val bitmap: Bitmap?, val message: String?, val target: ImageView)
