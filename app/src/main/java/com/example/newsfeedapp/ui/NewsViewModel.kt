package com.example.newsfeedapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsfeedapp.common.Resource
import com.example.newsfeedapp.data.model.Article
import com.example.newsfeedapp.domain.NewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel  @Inject constructor (private val newsUseCase: NewsUseCase) : ViewModel() {

    private var articleNews = MutableLiveData<Resource<Article>>()
     var error = MutableLiveData<Boolean>()




     fun getHomeNews() {
        articleNews.postValue(Resource.Loading())

        viewModelScope.launch(Dispatchers.IO) {
            val result = newsUseCase.getNewsUseCase()

            articleNews.postValue(Resource.Success(result))
            if(result.isNullOrEmpty()){
                //articleNews.postValue(Resource.Error(msg="No data saved "))
                error.postValue(true)
            }
        }
    }

    fun getNews() = articleNews as LiveData<Resource<Article>>


    suspend fun updateFavorite(isFv:Int,url:String)=newsUseCase.updateFavoriteUseCase(isFv,url)




}
