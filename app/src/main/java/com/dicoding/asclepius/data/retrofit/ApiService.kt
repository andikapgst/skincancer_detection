package com.dicoding.asclepius.data.retrofit

import com.dicoding.asclepius.data.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines?q=cancer&category=health&language=en")
    suspend fun getCancerNews(@Query("apiKey") apiKey: String): NewsResponse
}