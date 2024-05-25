package com.app.movies.views.viewModels

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import com.app.movies.data.model.ApiResponse
import com.app.movies.data.model.ResponseState
import com.app.movies.data.repositories.NetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.setMain
import retrofit2.Response

@ExperimentalCoroutinesApi
class NetworkViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: NetworkViewModel
    private lateinit var repository: NetworkRepository

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = NetworkViewModel(repository)
    }

    @Test
    fun `test getVideos returns success`() = runTest {
        val mockResponse = ApiResponse(
            dates = ApiResponse.Dates("2024-06-19", "2024-05-29"),
            page = 1,
            results = arrayListOf(
                ApiResponse.Results(
                    id = 1,
                    title = "Mock Movie",
                    overview = "Mock overview",
                    releaseDate = "2024-05-22",
                    voteAverage = 8.0
                )
            ),
            totalPages = 1,
            totalResults = 1
        )

        coEvery { repository.getVideosUpComing() } returns flow {
            emit(ResponseState.Success(mockResponse.results))
        }


        viewModel.getVideos()

        val result = viewModel.videos.first()

        assert(result is ResponseState.Success)

        coVerify { repository.getVideosUpComing() }
    }

    @Test
    fun `test getVideos returns error`() = runTest {
        val mockError = "Network Error"


        coEvery { repository.getVideosUpComing() } returns flow {
            throw Exception(mockError)
        }

        viewModel.getVideos()

        val result = viewModel.videos.first()

        assert(result is ResponseState.Error)

        coVerify { repository.getVideosUpComing() }
    }

    @Test
    fun `test getSearchVideos returns success`() = runTest {
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

        val mockResponse: Response<ApiResponse> = Response.success(mockSearchResponse)

        coEvery { repository.searchMoviesByQuery(any()) } returns flow {
            emit(mockResponse)
        }

        viewModel.getSearchVideos("query")

        val result = viewModel.searchedVideos.first()
        println("Actual value: $result")
        println("Expected value: $mockResponse")
        assert(result is ResponseState.Success)

        coVerify { repository.searchMoviesByQuery("query") }
    }



    @Test
    fun `test getSearchVideos returns error`() = runTest {
        val mockError = "Network Error"

        coEvery { repository.searchMoviesByQuery(any()) } returns flow {
            throw Exception(mockError)
        }

        viewModel.getSearchVideos("query")

        val result = viewModel.searchedVideos.first()

        assert(result is ResponseState.Error)

        coVerify { repository.searchMoviesByQuery("query") }
    }

}
