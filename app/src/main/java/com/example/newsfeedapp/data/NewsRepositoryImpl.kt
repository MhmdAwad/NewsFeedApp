package com.example.newsfeedapp.data


import android.content.SharedPreferences
import com.example.newsfeedapp.common.NetworkAwareHandler
import com.example.newsfeedapp.common.SharedPrefHelper
import com.example.newsfeedapp.data.model.Article
import com.example.newsfeedapp.data.sources.homeCahedData.OfflineDataSource
import com.example.newsfeedapp.data.sources.remoteApi.OnlineDataSource
import com.example.newsfeedapp.domain.NewsRepository
import javax.inject.Inject


class NewsRepositoryImpl @Inject constructor(
    private val offlineDataSource: OfflineDataSource,
    private val onlineDataSource: OnlineDataSource,
    private val sharedPreferences: SharedPrefHelper,
    private val networkHandler: NetworkAwareHandler
): NewsRepository {


    override suspend fun getNewsSources(isDataUpdated:Boolean): List<Article> {

        // you can change this logic depend on the business requirements
        return if (networkHandler.isOnline()) {

            if (sharedPreferences.runOnceADay()||isDataUpdated)
                 cacheArticles(getRemoteData())
            getCachedData()
        } else {
            getCachedData()
        }
    }



    override suspend fun updateFavorite(isFv: Int, url: String) = offlineDataSource.updateFav(isFv, url)

    private suspend fun cacheArticles(data: List<Article>) = offlineDataSource.cacheArticles(data)
    private fun getCachedData(): List<Article> = offlineDataSource.getArticles()
    private suspend fun getRemoteData(): List<Article> = onlineDataSource.getArticles()




}

