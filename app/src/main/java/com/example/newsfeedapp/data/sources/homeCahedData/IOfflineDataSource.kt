package com.example.newsfeedapp.data.sources.homeCahedData

import androidx.room.Query
import com.example.newsfeedapp.data.model.Article

interface IOfflineDataSource {
     fun getArticles(): List<Article> = emptyList()

    suspend fun cacheArticles(data: List<Article>){}

    suspend fun updateFav(isFv: Int, url: String){}



}
