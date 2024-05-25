package com.app.movies.data.repositories

import android.content.Context
import com.app.movies.data.local.db.LocalDatabase
import com.app.movies.data.model.ApiResponse
import com.app.movies.data.model.ResponseState
import com.app.movies.data.model.VideoResponseModel
import com.app.movies.data.source.TMDBService
import com.app.movies.data.utils.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class NetworkRepository(context: Context, private val service : TMDBService) {

    var movieDao = LocalDatabase.getInstance(context).videosDao()
    suspend fun getVideosUpComing(): Flow<ResponseState<List<ApiResponse.Results>>> = flow {
        val movies = movieDao.getAllMovies()
        try {
            val response = service.getUpComingMovies()
            if (response.isSuccessful) {
                val results = response.body()?.results ?: emptyList()
                movieDao.insertAll(results.map { it.toEntity() })
                emit(ResponseState.Success(results))
            } else {
                if (movies.isNotEmpty()) {
                    emit(ResponseState.Success(movies.map { it.toResult() }))
                } else {
                    emit(ResponseState.Error("Error fetching movies"))
                }
            }
        } catch (e: Exception) {
            if (movies.isNotEmpty()) {
                emit(ResponseState.Success(movies.map { it.toResult() }))
            } else {
                emit(ResponseState.Error("Error fetching movies: ${e.message}"))
            }
        }
    }


/*
    suspend fun getVideosUpComing() : Flow<Response<ApiResponse>> = flow {
        emit(service.getUpComingMovies())
    }
*/

    suspend fun getVideosLinks(id : Int) : Flow<Response<VideoResponseModel>> = flow {
        emit(service.getVideos(id))
    }
    suspend fun searchMoviesByQuery(query : String) : Flow<Response<ApiResponse>> = flow {
        emit(service.searchMoviesByQuery(1, query))
    }
}