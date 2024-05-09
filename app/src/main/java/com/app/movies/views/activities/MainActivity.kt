package com.app.movies.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.movies.data.model.ApiResponse
import com.app.movies.data.model.ResponseState
import com.app.movies.data.utils.Constants
import com.app.movies.data.utils.afterDelay
import com.app.movies.data.utils.beGone
import com.app.movies.data.utils.beVisible
import com.app.movies.databinding.ActivityMainBinding
import com.app.movies.views.adapters.MovieAdapter
import com.app.movies.views.viewModels.NetworkViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    val networkViewModel by viewModel<NetworkViewModel>()
    private var resultsAdapter: MovieAdapter ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNetworkObserver()
        afterDelay(500){
            networkViewModel.getVideos()
        }
    }

    private fun initNetworkObserver() {
        lifecycleScope.launch {
            networkViewModel.videos.collect { responseState ->
                when (responseState) {
                    is ResponseState.Loading -> {
                        binding.loadingAnimation.beVisible()
                    }

                    is ResponseState.Error -> {
                        binding.loadingAnimation.beVisible()
                    }

                    is ResponseState.Success -> {
                        binding.loadingAnimation.beGone()
                        val resultsList = responseState.data.results
                        initRecycler(resultsList)
                    }
                }
            }
        }
    }

    private fun initRecycler(resultsList: ArrayList<ApiResponse.Results>) {
        resultsAdapter = MovieAdapter(resultsList) { selectedMovie ->

            val intent = Intent(this@MainActivity, MovieDetailActivity::class.java)
            intent.putExtra(Constants.movieItem, selectedMovie)
            startActivity(intent)
        }
        binding.moviesRecyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.moviesRecyclerview.adapter = resultsAdapter
    }
}