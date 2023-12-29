package com.example.strikescore

import com.example.strikescore.data.AppContainer
import android.app.Application
import com.example.strikescore.data.DefaultAppContainer

/**
 * Custom Application class for the Strike Score application.
 *
 * This class extends [Application] and is responsible for initializing the application-wide
 * [AppContainer] using [DefaultAppContainer].
 */
class StrikeScoreApplication : Application() {
    lateinit var container: AppContainer

    /**
     * Overrides the onCreate method to initialize the [AppContainer].
     */
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(context = applicationContext)
    }
}