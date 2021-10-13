package com.example.newsfeedapp.common

import android.content.Context
import android.content.SharedPreferences
import java.util.*
import javax.inject.Inject

class SharedPrefHelper @Inject constructor(private val sharedPreferences: SharedPreferences, private val editor: SharedPreferences.Editor) {


    private fun setDay(now: Long) {
        editor.putLong(KEY_CURRENT_DAY, now)

        editor.commit()
    }




    private fun getLastDay(): Long {
        return sharedPreferences.getLong(KEY_CURRENT_DAY, 0)
    }

    fun runOnceADay(): Boolean {

        val lastCheckedMillis = getLastDay()// "KEY" you may change yhe value
        val now = System.currentTimeMillis()
        val diffMillis = now - lastCheckedMillis
         if (diffMillis >= 3600000 * 24) { // set up your time circulation
            setDay(now)
            return true
        }
          return false




    }
}