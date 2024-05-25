package com.app.movies.data.source

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object OkHttpProvider {

    val instance: OkHttpClient

    init {
        val logger = HttpLoggingInterceptor { }
        logger.level = HttpLoggingInterceptor.Level.BASIC

        instance = OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()
            val originalHttpUrl = original.url

            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", "9a8f1f6f56b8a39e9b08e31779bd4fbb")
                .build()

            val request = original.newBuilder().url(url).build()
            chain.proceed(request)
        }.addInterceptor(logger)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }
}