package com.example.newsfeedapp.data.sources.homeCahedData

import com.example.newsfeedapp.data.model.Article
import javax.inject.Inject

interface OfflineDataSource {
    fun getArticles(): List<Article> = emptyList()

    suspend fun cacheArticles(data: List<Article>) {}

    suspend fun updateFav(isFv: Int, url: String) {}


}


class OfflineDataSourceImpl @Inject constructor(private val homeDao: HomeNewsDao) :
    OfflineDataSource {

    override fun getArticles(): List<Article> = homeDao.getAllArticles()

    override suspend fun cacheArticles(data: List<Article>) {
        homeDao.insertList(data)
    }

    override suspend fun updateFav(isFv: Int, url: String) {
        homeDao.updateFav(isFv, url)
    }


}