package com.example.newsfeedapp.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsfeedapp.common.Resource
import com.example.newsfeedapp.data.NewsRepository
import com.example.newsfeedapp.data.model.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModel  @ViewModelInject constructor (private val newsRepository: NewsRepository) : ViewModel() {

    private var articleNews = MutableLiveData<Resource<Article>>()
     var error = MutableLiveData<Boolean>()




     fun getHomeNews() {
        articleNews.postValue(Resource.Loading())

        viewModelScope.launch(Dispatchers.IO) {
            val result = newsRepository.getNewsSources()

            articleNews.postValue(Resource.Success(result))
            if(result.isNullOrEmpty()){
                //articleNews.postValue(Resource.Error(msg="No data saved "))
                error.postValue(true)
            }
        }
    }

    fun getNews() = articleNews as LiveData<Resource<Article>>


    suspend fun updateFavorite(isFv:Int,url:String)=newsRepository.updateFavorite(isFv,url)




}
