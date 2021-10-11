package  com.example.newsfeedapp.di


import android.content.Context
import com.example.newsfeedapp.common.NetworkAwareHandler
import com.example.newsfeedapp.common.NetworkHandlerImpl
import com.example.newsfeedapp.data.*
import com.example.newsfeedapp.data.sources.homeCahedData.*
import com.example.newsfeedapp.data.sources.remoteApi.*
import com.example.newsfeedapp.domain.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    @Singleton
    fun provideNewsRepository(iOfflineDataSource: OfflineDataSource, iOnlineDataSource: OnlineDataSource, iNetworkAwareHandler: NetworkAwareHandler)
            = NewsRepositoryImpl(iOfflineDataSource,iOnlineDataSource,iNetworkAwareHandler) as NewsRepository

    @Provides
    @Singleton
    fun provideIOfflineDataSource (homeDao: HomeNewsDao)
            = OfflineDataSourceImpl(homeDao) as OfflineDataSource

    @Provides
    @Singleton
    fun provideIOnlineDataSource( service: ApiHelper)
            = OnlineDataSourceImpl(service) as OnlineDataSource

    @Provides
    @Singleton
    fun provideINetworkAwareHandler( @ApplicationContext context: Context)
            =NetworkHandlerImpl(context) as NetworkAwareHandler


    @Provides
    @Singleton
    fun provideIApiHelper( apiService: ApiService)
            =ApiHelperImpl(apiService) as ApiHelper







}


/*

val repoModule = module {

    // Provide NewsRepository
    single { NewsRepository(get() , get() , get() ) }

    factory  <IOfflineDataSource>{ OfflineSourcesRoomBased(get()) }

    factory <IOnlineDataSource> { OnlineSourcesBasedRetroFit(get())  }

    single <INetworkAwareHandler> { NetworkHandler(get())  }

    single { FavRepo(get()) }


    factory <IApiHelper> { ApiHelperImpl(get())  }


}

 */
