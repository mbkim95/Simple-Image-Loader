package com.example.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface ImageDownloadService {
    @GET
    fun downloadImage(@Url imageUrl: String): Call<ResponseBody>
}