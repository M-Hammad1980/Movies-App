package com.app.movies.data.repositories

import com.app.movies.data.model.ApiResponse
import com.app.movies.data.source.TMDBService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class NetworkRepository(private val service : TMDBService) {
    suspend fun getVideosUpComing() : Flow<Response<ApiResponse>> = flow {
        emit(service.getUpComingMovies())
    }
}