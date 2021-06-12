package com.example.coinhakodemo.di

import com.example.coinhakodemo.module.home.HomeViewModel
import com.example.coinhakodemo.repository.HomeApi
import com.example.coinhakodemo.repository.HomeRemoteDataSource
import com.example.coinhakodemo.repository.HomeRepository
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.InternalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InternalCoroutinesApi
val viewModelModule = module {
    viewModel {
        HomeViewModel(get())
    }
}

val remoteDataSourceModule= module {
    single {
        HomeRemoteDataSource(get())
    }
}

val repositoryModule = module {
    single {
        HomeRepository(get())
    }
}

val apiModule = module {
    fun provideUseApi(retrofit: Retrofit): HomeApi {
        return retrofit.create(HomeApi::class.java)
    }

    single { provideUseApi(get()) }
}

val retrofitModule = module {

    fun provideGson(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
    }

    fun provideHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(factory: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.coinhako.com/api/")
            .addConverterFactory(GsonConverterFactory.create(factory))
            .client(client)
            .build()
    }

    single { provideGson() }
    single { provideHttpClient() }
    single { provideRetrofit(get(), get()) }
}