package com.app.movies.views.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.movies.data.model.ApiResponse
import com.app.movies.data.model.ResponseState
import com.app.movies.data.model.VideoResponseModel
import com.app.movies.data.repositories.NetworkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NetworkViewModel(private val repository: NetworkRepository) : ViewModel() {
    private val _videos: MutableStateFlow<ResponseState<List<ApiResponse.Results>>> =
        MutableStateFlow(ResponseState.Loading)
    val videos: StateFlow<ResponseState<List<ApiResponse.Results>>> = _videos

    private val _videosLinks: MutableStateFlow<ResponseState<VideoResponseModel>> =
        MutableStateFlow(ResponseState.Loading)
    val videosLinks: StateFlow<ResponseState<VideoResponseModel>> = _videosLinks

    private val _searchedVideo: MutableStateFlow<ResponseState<ApiResponse>> =
        MutableStateFlow(ResponseState.Loading)
    val searchedVideos: StateFlow<ResponseState<ApiResponse>> = _searchedVideo


    fun getVideos()
    = viewModelScope.launch {
        repository.getVideosUpComing()
            .onStart {
                _videos.value = ResponseState.Loading
                Log.e("tag*", "repo : loading")
            }
            .catch {error ->
                _videos.value = ResponseState.Error(error.message.toString())
                Log.e("tag*", "repo : error : ${error.message.toString()}")
            }
            .collect {response->
//                _videos.value = ResponseState.Success(response)
                _videos.value = response
                Log.e("tag*", "repo : response: ${response}")
            }
    }

    fun getVideosLinks(id : Int)
    = viewModelScope.launch {
        repository.getVideosLinks(id)
            .onStart {
                _videosLinks.value = ResponseState.Loading
                Log.e("tag*", "repo : loading")
            }
            .catch {error ->
                _videosLinks.value = ResponseState.Error(error.message.toString())
                Log.e("tag*", "repo : error : ${error.message.toString()}")
            }
            .collect {response->
//                _videosLinks.value = ResponseState.Success(response.body()!!)
                val videoResponseModel = VideoResponseModel(id, response.body()!!.videoResults)
                _videosLinks.value = ResponseState.Success(videoResponseModel)
                Log.e("tag*", "repo : response: ${response.body()}")
            }
    }


    fun getSearchVideos(query : String)
    = viewModelScope.launch {
        repository.searchMoviesByQuery(query)
            .onStart {
                _searchedVideo.value = ResponseState.Loading
                Log.e("tag*", "repo : loading")
            }
            .catch {error ->
                _searchedVideo.value = ResponseState.Error(error.message.toString())
                Log.e("tag*", "repo : error : ${error.message.toString()}")
            }
            .collect {response->
                _searchedVideo.value = ResponseState.Success(response.body()!!)
                Log.e("tag*", "repo : response: ${response.body()}")
            }
    }
}