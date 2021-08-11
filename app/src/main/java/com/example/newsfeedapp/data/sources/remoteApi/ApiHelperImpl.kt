package com.example.newsfeedapp.data.sources.remoteApi

import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ApiHelperImpl @Inject constructor (private val apiService: ApiService) : IApiHelper {

    override fun getArticles(source: String)= flow { emit(apiService.getArticlesNews(source)) }
}
