package com.example.newsfeedapp.common

import android.app.Application
import com.example.newsfeedapp.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        //setup timber for debug only
        if(BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}