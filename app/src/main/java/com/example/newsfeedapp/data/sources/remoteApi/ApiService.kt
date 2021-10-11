package com.example.newsfeedapp.data.sources.remoteApi

import com.example.newsfeedapp.common.END_POINT
import com.example.newsfeedapp.common.SOURCE
import com.example.newsfeedapp.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(END_POINT)
    suspend fun getArticlesNews(@Query(SOURCE) sourceName: String): NewsResponse
}