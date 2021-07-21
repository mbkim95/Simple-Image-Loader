package com.example

object Constants {
    const val MIN_DISK_CACHE_SIZE = (5 * 1024 * 1024).toLong() // 5MB
    const val MAX_DISK_CACHE_SIZE = (50 * 1024 * 1024).toLong() // 50MB

    const val NO_PLACEHOLDER = 0

    const val BASE_URL = "https://developers.kakao.com/" // retrofit 객체 생성을 위한 임시 url

    const val IMAGE_DOWNLOAD_COMPLETE = 1
    const val IMAGE_DOWNLOAD_FAIL = 2
}