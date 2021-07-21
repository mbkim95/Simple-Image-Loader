package com.example.model

import android.graphics.Bitmap

data class DecodingResult(
    val bitmap: Bitmap?,
    val error: String?,
    val callback: (bitmap: Bitmap?) -> Unit
)