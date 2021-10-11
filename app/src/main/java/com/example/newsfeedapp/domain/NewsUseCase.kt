package com.example.newsfeedapp.domain

import com.example.newsfeedapp.data.model.Article
import javax.inject.Inject




class NewsUseCase @Inject constructor(private val repository: NewsRepository) {


     suspend fun getNewsUseCase(): List<Article> {
        return repository.getNewsSources()
    }



    suspend fun updateFavoriteUseCase(isFv:Int,url:String)=repository.updateFavorite(isFv,url)

}