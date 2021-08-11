package com.example.newsfeedapp.data.sources.remoteApi


import android.util.Log
import com.example.newsfeedapp.data.model.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class OnlineSourcesBasedRetroFit @Inject constructor (private val service: IApiHelper) : IOnlineDataSource {

    private val articlesNews = mutableListOf<Article>()

    companion object {
        var errorMsg: String = ""
    }

/*
make parallel call with kotlin flow by using zip operator

zip operator, it makes both the network calls in parallel and gives the results of both the network calls in a single callback
 when both the network calls are completed.

By zipping two flow collections using the Zip operator, both the network calls run in parallel. And we get the result when both finish.
 In this way, we get the results of both the flow collections at a time.

 */

    @ExperimentalCoroutinesApi
    override suspend fun getArticles(): List<Article> {
        service.getArticles("the-next-web")
            .zip(service.getArticles("associated-press")) { firstSource, secondSource ->
                Log.e("TAG","calling api")
                return@zip mutableListOf<Article>().apply {
                    firstSource.articles?.let { addAll(it) }
                    secondSource.articles?.let { addAll(it) }
                }
            }.flowOn(Dispatchers.IO)
            .catch { e ->
                errorMsg = e.message.toString()
                Log.e("TAG", errorMsg)
            }
            .collect {
                articlesNews.addAll(it)
            }
        return articlesNews
    }



    /*
        make parallel call with kotlin coroutines  by using async and await

    override suspend fun getArticles(): List<Article> {

        try {
            // coroutineScope is needed, else in case of any network error, it will crash
            coroutineScope {
                val articleSourceTheNextWeb = async { service.getArticlesNews("the-next-web") }
                val articleSourceAssociatedPress =
                    async { service.getArticlesNews("associated-press") }
                val firstSource = articleSourceTheNextWeb.await()
                val secondSource = articleSourceAssociatedPress.await()
                val allArticlesFromApi = mutableListOf<Article>()
                firstSource.articles?.let { allArticlesFromApi.addAll(it) }
                secondSource.articles?.let { allArticlesFromApi.addAll(it) }
                articlesNews.addAll(allArticlesFromApi)
            }
        } catch (e: Exception) {
            errorMsg= e.message.toString()
            Log.e("TAG", errorMsg)

        }
        return articlesNews
    }
        */




}
