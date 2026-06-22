package com.onlyburger.app

import android.app.Application
import com.onlyburger.app.di.AppContainer

/**
 * Application subclass that owns the [AppContainer] for the whole process.
 * Registered in AndroidManifest.xml via android:name=".OnlyBurgerApp".
 */
class OnlyBurgerApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
