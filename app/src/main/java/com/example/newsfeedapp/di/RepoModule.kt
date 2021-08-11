package  com.example.newsfeedapp.di


import android.content.Context
import androidx.room.Room
import com.example.newsfeedapp.common.INetworkAwareHandler
import com.example.newsfeedapp.common.NetworkHandler
import com.example.newsfeedapp.data.*

import com.example.newsfeedapp.data.sources.homeCahedData.HomeNewsDao
import com.example.newsfeedapp.data.sources.homeCahedData.IOfflineDataSource
import com.example.newsfeedapp.data.sources.homeCahedData.OfflineSourcesRoomBased
import com.example.newsfeedapp.data.sources.remoteApi.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import org.koin.dsl.module
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepoModule {

    @Provides
    @Singleton
    fun provideNewsRepository(iOfflineDataSource: IOfflineDataSource,iOnlineDataSource: IOnlineDataSource,iNetworkAwareHandler: INetworkAwareHandler)
            = NewsRepository(iOfflineDataSource,iOnlineDataSource,iNetworkAwareHandler)

    @Provides
    @Singleton
    fun provideIOfflineDataSource (homeDao: HomeNewsDao)
            = OfflineSourcesRoomBased(homeDao) as IOfflineDataSource

    @Provides
    @Singleton
    fun provideIOnlineDataSource( service: IApiHelper)
            = OnlineSourcesBasedRetroFit(service) as IOnlineDataSource

    @Provides
    @Singleton
    fun provideINetworkAwareHandler( @ApplicationContext context: Context)
            =NetworkHandler(context) as INetworkAwareHandler


    @Provides
    @Singleton
    fun provideIApiHelper( apiService: ApiService)
            =ApiHelperImpl(apiService) as IApiHelper







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
