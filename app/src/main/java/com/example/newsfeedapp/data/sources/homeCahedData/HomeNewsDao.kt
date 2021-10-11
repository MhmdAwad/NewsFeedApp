package com.example.newsfeedapp.data.sources.homeCahedData

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsfeedapp.data.model.Article


@Dao
interface HomeNewsDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertList(article: List<Article>): List<Long>


    @Query("SELECT * FROM  Article")
    fun getAllArticles(): List<Article>


    @Query("UPDATE Article SET isFav = :isFv  WHERE url = :url")
    suspend fun updateFav(isFv: Int, url: String)


}