package com.app.movies.data.source

import com.app.movies.data.model.ApiResponse
import com.app.movies.data.model.VideoResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBService {

    @GET("movie/upcoming")
    suspend fun getUpComingMovies(
        @Query("api_key") apiKey: String = "9a8f1f6f56b8a39e9b08e31779bd4fbb"
    ): Response<ApiResponse>

    @GET("movie/{id}/videos")
    suspend fun getVideos(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = "9a8f1f6f56b8a39e9b08e31779bd4fbb"
    ): Response<VideoResponseModel>
}