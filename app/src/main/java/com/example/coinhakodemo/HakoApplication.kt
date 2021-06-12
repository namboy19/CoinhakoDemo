package com.example.coinhakodemo

import android.app.Application
import com.example.coinhakodemo.di.*
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@InternalCoroutinesApi
class HakoApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@HakoApplication)
            modules(
                listOf(
                    repositoryModule,
                    remoteDataSourceModule,
                    viewModelModule,
                    retrofitModule,
                    apiModule
                )
            )
        }
    }
}