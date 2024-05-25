package com.app.movies.repositories

import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.app.movies.data.local.db.LocalDatabase
import com.app.movies.data.local.db.dao.MovieDao
import com.app.movies.data.model.ApiResponse
import com.app.movies.data.model.ResponseState
import com.app.movies.data.model.VideoResponseModel
import com.app.movies.data.repositories.NetworkRepository
import com.app.movies.data.source.TMDBService
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import retrofit2.Response

@ExperimentalCoroutinesApi
class NetworkRepositoryTest {

    private lateinit var repository: NetworkRepository
    private lateinit var service: TMDBService
    private lateinit var movieDao: MovieDao

    @Before
    fun setUp() {
        service = mockk()
        movieDao = mockk()
        repository = NetworkRepository(mockk(), service)
        repository.movieDao = movieDao
    }

    @Test
    fun `getVideosUpComing success from API`() = runBlocking {

        val mockUpComingResponse = ApiResponse(
            dates = null,
            page = 1,
            results = arrayListOf(
                ApiResponse.Results(
                    id = 1,
                    title = "Mock Searched Movie",
                    overview = "Mock overview",
                    releaseDate = "2024-05-22",
                    voteAverage = 8.0
                )
            ),
            totalPages = 1,
            totalResults = 1
        )

        val mockResponse: Response<ApiResponse> = Response.success(mockUpComingResponse)
        coEvery { service.getUpComingMovies() } returns mockResponse

        coEvery { movieDao.getAllMovies() } returns emptyList()
        coEvery { movieDao.insertAll(any()) } returns Unit

        val result = repository.getVideosUpComing().toList()

        assertEquals(1, result.size)

    }

    @Test
    fun `getVideosUpComing error from API`() = runBlocking {

        val errorMessage = "Error fetching movies"
        coEvery { service.getUpComingMovies() } returns Response.error(404, ResponseBody.create(null, errorMessage))


        coEvery { movieDao.getAllMovies() } returns emptyList()


        val result = repository.getVideosUpComing().toList()

        assertEquals(1, result.size)
        assertEquals(ResponseState.Error(errorMessage), result[0])
    }

    @Test
    fun `getVideosLinks success from API`() = runBlocking {
        val mockVideoResponse = VideoResponseModel(
            id = 123,
            videoResults = arrayListOf(
                VideoResponseModel.VideoResult(
                    iso6391 = "en",
                    iso31661 = "US",
                    name = "Mock Video",
                    key = "mock_key",
                    site = "YouTube",
                    size = 720,
                    type = "Trailer",
                    official = true,
                    publishedAt = "2024-05-22",
                    id = "abcd1234"
                )
            )
        )
        coEvery { service.getVideos(any()) } returns Response.success(mockVideoResponse)

        val result = repository.getVideosLinks(123).toList()

        assertEquals(1, result.size)
    }

    @Test
    fun `searchMoviesByQuery success from API`() = runBlocking {
        val mockSearchResponse = ApiResponse(
            dates = null,
            page = 1,
            results = arrayListOf(
                ApiResponse.Results(
                    id = 1,
                    title = "Mock Searched Movie",
                    overview = "Mock overview",
                    releaseDate = "2024-05-22",
                    voteAverage = 8.0
                )
            ),
            totalPages = 1,
            totalResults = 1
        )
        coEvery { service.searchMoviesByQuery(any(), any()) } returns Response.success(mockSearchResponse)

        val result = repository.searchMoviesByQuery("query").toList()

        assertEquals(1, result.size)
    }

    @Test
    fun `getVideosLinks error from API`() = runBlocking {
        val errorMessage = "Error fetching videos"
        coEvery { service.getVideos(any()) } returns Response.error(404, ResponseBody.create(null, errorMessage))

        val result = repository.getVideosLinks(123).toList()

        assertEquals(1, result.size)
        assertEquals(Response.error<VideoResponseModel>(404, ResponseBody.create(null, errorMessage)).message(), result[0].message())
    }

    @Test
    fun `searchMoviesByQuery error from API`() = runBlocking {
        val errorMessage = "Error searching movies"
        coEvery { service.searchMoviesByQuery(any(), any()) } returns Response.error(404, ResponseBody.create(null, errorMessage))

        val result = repository.searchMoviesByQuery("query").toList()

        assertEquals(1, result.size)
        assertEquals(Response.error<ApiResponse>(404, ResponseBody.create(null, errorMessage)).message(), result[0].message())
    }
}

