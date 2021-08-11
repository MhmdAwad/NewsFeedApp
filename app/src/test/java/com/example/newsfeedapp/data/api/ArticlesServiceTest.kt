package com.example.newsfeedapp.data.api



import com.example.newsfeedapp.data.model.Article
import com.example.newsfeedapp.data.sources.remoteApi.ApiService
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException

@RunWith(JUnit4::class)
class ArticlesServiceTest : BaseServiceTest<ApiService>() {

    private lateinit var service: ApiService
    private lateinit var results: Article


    @Before
    fun initService() {
        service = createService(ApiService::class.java)
    }

    @Throws(IOException::class)
    @Test
    fun fetchArticlesListTest()   {
        enqueueResponse("/NewsResponse.json")
        runBlocking {
            results = requireNotNull(service.getArticlesNews("the-next-web").articles?.get(0))
        }
        mockWebServer.takeRequest()

        assertThat(results.author,`is`("Thomas Macaulay"))



    }


}
