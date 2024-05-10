package com.app.movies.data.source

import com.app.movies.data.model.ApiResponse
import com.app.movies.data.model.VideoResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBService {

    @GET("movie/upcoming")
    suspend fun getUpComingMovies(): Response<ApiResponse>

    @GET("movie/{id}/videos")
    suspend fun getVideos(
        @Path("id") id: Int
    ): Response<VideoResponseModel>


    @GET("search/movie")
    suspend fun searchMoviesByQuery(
        @Query("page") page: Int = 1,
        @Query("query") query: String
    ): Response<ApiResponse>

}