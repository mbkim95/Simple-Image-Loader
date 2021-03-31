package com.example

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.core.content.ContextCompat

fun calculateMemoryCacheSize(context: Context): Int {
    val am = ContextCompat.getSystemService(context, ActivityManager::class.java)
    val largeHeap = context.applicationInfo.flags and ApplicationInfo.FLAG_LARGE_HEAP != 0
    val memoryClass = if (largeHeap) am!!.largeMemoryClass else am!!.memoryClass
    // Target ~15% of the available heap.
    return (1024L * 1024L * memoryClass / 7).toInt()
}