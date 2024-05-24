package com.app.movies.data.repositories

import android.content.Context
import com.app.movies.data.model.ApiResponse
import com.app.movies.data.model.VideoResponseModel
import com.app.movies.data.source.TMDBService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class NetworkRepository(context: Context, private val service : TMDBService) {
    suspend fun getVideosUpComing() : Flow<Response<ApiResponse>> = flow {
        emit(service.getUpComingMovies())
    }

    suspend fun getVideosLinks(id : Int) : Flow<Response<VideoResponseModel>> = flow {
        emit(service.getVideos(id))
    }
    suspend fun searchMoviesByQuery(query : String) : Flow<Response<ApiResponse>> = flow {
        emit(service.searchMoviesByQuery(1, query))
    }
}