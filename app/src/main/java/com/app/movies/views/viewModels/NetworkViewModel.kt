package com.app.movies.views.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.movies.data.model.ApiResponse
import com.app.movies.data.model.ResponseState
import com.app.movies.data.repositories.NetworkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NetworkViewModel(private val repository: NetworkRepository) : ViewModel() {
    private val _videos: MutableStateFlow<ResponseState<ApiResponse>> =
        MutableStateFlow(ResponseState.Loading)
    val videos: StateFlow<ResponseState<ApiResponse>> = _videos


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
                _videos.value = ResponseState.Success(response.body()!!)
                Log.e("tag*", "repo : response: ${response.body()}")
            }
    }

}