package com.example

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.StatFs
import androidx.core.content.ContextCompat
import java.io.File
import kotlin.math.max
import kotlin.math.min

fun calculateMemoryCacheSize(context: Context): Int {
    val am = ContextCompat.getSystemService(context, ActivityManager::class.java)
    val largeHeap = context.applicationInfo.flags and ApplicationInfo.FLAG_LARGE_HEAP != 0
    val memoryClass = if (largeHeap) am!!.largeMemoryClass else am!!.memoryClass
    // Target ~15% of the available heap.
    return (1024L * 1024L * memoryClass / 7).toInt()
}

fun calculateDiskCacheSize(dir: File): Long {
    var size = Constants.MIN_DISK_CACHE_SIZE

    try {
        val statFs = StatFs(dir.absolutePath)
        val blockCount = statFs.blockCountLong
        val blockSize = statFs.blockSizeLong
        val available = blockCount * blockSize
        size = (available / 50)
    } catch (ignored: IllegalArgumentException) {
    }
    return max(min(size, Constants.MAX_DISK_CACHE_SIZE), Constants.MIN_DISK_CACHE_SIZE)
}