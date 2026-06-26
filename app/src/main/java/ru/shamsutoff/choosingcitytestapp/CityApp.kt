package ru.shamsutoff.choosingcitytestapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.shamsutoff.choosingcitytestapp.di.appModule

class CityApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CityApp)
            modules(appModule)
        }
    }
}
