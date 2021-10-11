package com.example.newsfeedapp.data.sources.homeCahedData

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.example.newsfeedapp.data.model.Article
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.Is
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class HomeNewsDataBaseTest{
    private lateinit var dataDao: HomeNewsDao
    private lateinit var db: HomeNewsDataBase
    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, HomeNewsDataBase::class.java)
            .build()

        dataDao = db.getHomeNewsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {

        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetData() {
        val fakeList= mutableListOf<Article>().apply {
            add(
                Article(
                author = "one",
                url = "a",
                publishedAt = "b",
                description = "c",
                urlToImage = "d",
                title = "e")
            )
        }
        runBlocking {
            dataDao.insertList(fakeList)
        }
        val allData = dataDao.getAllArticles()
        assertThat(allData.size, Is.`is`(1))

    }

}