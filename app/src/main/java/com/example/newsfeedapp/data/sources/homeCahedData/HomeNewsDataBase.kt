package com.example.newsfeedapp.data.sources.homeCahedData

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsfeedapp.data.model.Article

@Database(entities = [Article::class], version = 2 , exportSchema = false)

abstract class HomeNewsDataBase : RoomDatabase() {
    abstract fun getHomeNewsDao(): HomeNewsDao
}