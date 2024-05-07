package com.app.movies.di

import com.app.movies.data.repositories.NetworkRepository
import com.app.movies.data.source.TMDBService
import com.app.movies.data.utils.Constants
import com.app.movies.views.viewModels.NetworkViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object AppModule {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()


    val networkModule = module {
        single {
            Retrofit.Builder()
                .baseUrl(Constants.tmdbBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TMDBService::class.java)
        }
        single { NetworkRepository(get()) }
        viewModel { NetworkViewModel(get()) }

    }
}
