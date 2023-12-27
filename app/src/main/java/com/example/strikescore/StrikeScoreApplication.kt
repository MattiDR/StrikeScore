package com.example.strikescore

import com.example.strikescore.data.AppContainer
import android.app.Application
import com.example.strikescore.data.DefaultAppContainer

class StrikeScoreApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(context = applicationContext)
    }
}