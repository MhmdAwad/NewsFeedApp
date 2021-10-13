package com.example.newsfeedapp.domain

import com.example.newsfeedapp.data.model.Article

interface NewsRepository {

    suspend fun getNewsSources(isDataUpdated:Boolean): List<Article>

    suspend fun updateFavorite(isFv: Int, url: String)
}