package com.example.newsfeedapp.data.sources.remoteApi

import com.example.newsfeedapp.data.model.NewsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface ApiHelper {

    fun getArticles(source:String): Flow<NewsResponse>

}

class ApiHelperImpl @Inject constructor (private val apiService: ApiService) : ApiHelper {

    override fun getArticles(source: String)= flow { emit(apiService.getArticlesNews(source)) }
}
