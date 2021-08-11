package com.example.newsfeedapp.data


import com.example.newsfeedapp.common.INetworkAwareHandler
import com.example.newsfeedapp.data.model.Article
import com.example.newsfeedapp.data.sources.homeCahedData.IOfflineDataSource
import com.example.newsfeedapp.data.sources.remoteApi.IOnlineDataSource
import javax.inject.Inject


class NewsRepository @Inject constructor(
    private val offlineDataSource: IOfflineDataSource,
    private val onlineDataSource: IOnlineDataSource,
    private val networkHandler: INetworkAwareHandler) {


    suspend fun getNewsSources(): List<Article> {

        // you can change this logic depend on the business requirements
        return if (networkHandler.isOnline()) {

           // if (offlineDataSource.getArticles().isEmpty())
                offlineDataSource.cacheArticles(getRemoteData())

            getCachedData()
        } else {
            getCachedData()
        }
    }


    private fun getCachedData(): List<Article> = offlineDataSource.getArticles()
    private suspend fun getRemoteData(): List<Article> = onlineDataSource.getArticles()

    suspend fun updateFavorite(isFv: Int, url: String) = offlineDataSource.updateFav(isFv, url)


}

