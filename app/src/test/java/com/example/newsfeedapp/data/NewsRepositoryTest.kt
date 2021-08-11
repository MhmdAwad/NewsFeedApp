package com.example.newsfeedapp.data

import com.example.newsfeedapp.common.INetworkAwareHandler
import com.example.newsfeedapp.data.model.Article
import com.example.newsfeedapp.data.sources.homeCahedData.IOfflineDataSource
import com.example.newsfeedapp.data.sources.remoteApi.IOnlineDataSource
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.Is
import org.junit.Assert.assertThat
import org.junit.Test

class NewsRepositoryTest {

val fakeList= mutableListOf<Article>().apply {
        add(Article(
            author = "one",
            url = "",
            publishedAt = "",
            description = "",
            urlToImage = "",
            title = ""
        ))
        add(Article(
            author = "two",
            url = "",
            publishedAt = "",
            description = "",
            urlToImage = "",
            title = ""
        ))

    }



    @Test
    fun getNewsSources_andInsertit_inRoom(){
        runBlocking {
            val offlineDataSource = object : IOfflineDataSource {
                override fun getArticles(): List<Article> {
                    return fakeList
                }
            }
            val newsRepository = NewsRepository(offlineDataSource, object : IOnlineDataSource {},

                object : INetworkAwareHandler {})

            val result = newsRepository.getNewsSources()
            val exepected = listOf(
                Article(
                    author = "one",
                    url = "",
                    publishedAt = "",
                    description = "",
                    urlToImage = "",
                    title = ""
                ),
                Article(
                    author = "two",
                    url = "",
                    publishedAt = "",
                    description = "",
                    urlToImage = "",
                    title = ""
                )
            )
            assertThat(result, Is.`is`(exepected))
        }
    }


    @Test
    fun getNewsSources_withOnlineNetwork_thenReturnListOfSourcesFromOfflineDataSource() {
        // run blocking to call suspend function or Coroutines scope
        runBlocking {
            val offlineDataSource = object : IOfflineDataSource {
                override fun getArticles(): List<Article> {
                    return fakeList
                }
            }
            val newsRepository = NewsRepository(offlineDataSource, object : IOnlineDataSource {},

                object : INetworkAwareHandler {})

            val result = newsRepository.getNewsSources()
            val exepected = listOf(
                Article(
                    author = "one",
                    url = "",
                    publishedAt = "",
                    description = "",
                    urlToImage = "",
                    title = ""
                ),
                Article(
                    author = "two",
                    url = "",
                    publishedAt = "",
                    description = "",
                    urlToImage = "",
                    title = ""
                )
            )
            assertThat(result, Is.`is`(exepected))
        }

    }


    @Test
    fun getNewsSources_withOfflineNetwork_thenReturnListOfSourcesFromOfflineDataSource() {
        runBlocking {
            val offlineDataSource = object : IOfflineDataSource {
                override fun getArticles(): List<Article> {
                    return fakeList
                }
            }

            val newsRepository = NewsRepository(offlineDataSource,
                object : IOnlineDataSource {},
                object : INetworkAwareHandler {
                    override fun isOnline(): Boolean = false
                })

            val result = newsRepository.getNewsSources()
            val exepected = listOf(
                Article(
                    author = "one",
                    url = "",
                    publishedAt = "",
                    description = "",
                    urlToImage = "",
                    title = ""
                ),
                Article(
                    author = "two",
                    url = "",
                    publishedAt = "",
                    description = "",
                    urlToImage = "",
                    title = ""
                )
            )
            assertThat(result, Is.`is`(exepected))
        }

    }

    @Test
    fun getNewsSources_withOnlineNetwork_verifyGetSourcesFromNetworkCalled() {
        runBlocking {
            var isGetSourcesInvoked = false

            val onlineDataSource = object : IOnlineDataSource {
                override suspend fun getArticles(): List<Article> {
                    isGetSourcesInvoked = true
                    return listOf()
                }
            }

            val newsRepository = NewsRepository(object : IOfflineDataSource {},
                onlineDataSource,
                object : INetworkAwareHandler {})

            newsRepository.getNewsSources()

            assertThat(isGetSourcesInvoked, Is.`is`(true))
        }

    }

    @Test
    fun getNewsSources_withOnlineNetwork_verifyCacheArticlesCalled() {
        runBlocking {
            var isCachedInvoked = false

            val onlineDataSource = object : IOnlineDataSource {
                override suspend fun getArticles(): List<Article> {
                    return listOf()
                }
            }

            val offlineDataSource = object : IOfflineDataSource {
                override suspend fun cacheArticles(data: List<Article>) {
                    isCachedInvoked = true
                }
            }


            val newsRepository = NewsRepository(
                offlineDataSource,
                onlineDataSource,
                object : INetworkAwareHandler {})

            newsRepository.getNewsSources()

            assertThat(isCachedInvoked, Is.`is`(true))
        }
    }

    @Test
    fun getNewsSources_withOfflineNetwork_verifyGetSourcesFromOfflineDataSourceIsCalled() {
        runBlocking {
            var isGetSourcesInvoked = false

            val onlineDataSource = object : IOnlineDataSource {
                override suspend fun getArticles(): List<Article> {
                    isGetSourcesInvoked = true
                    return listOf()
                }
            }

            val newsRepository = NewsRepository(object : IOfflineDataSource {},
                onlineDataSource,
                object : INetworkAwareHandler {})

            newsRepository.getNewsSources()

            assertThat(isGetSourcesInvoked, Is.`is`(true))
        }
    }
}

