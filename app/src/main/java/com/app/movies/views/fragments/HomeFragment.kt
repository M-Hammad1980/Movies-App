package com.app.movies.views.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.movies.R
import com.app.movies.data.model.ApiResponse
import com.app.movies.data.model.ResponseState
import com.app.movies.data.utils.Constants
import com.app.movies.data.utils.afterDelay
import com.app.movies.data.utils.beGone
import com.app.movies.data.utils.beVisible
import com.app.movies.data.utils.showToast
import com.app.movies.databinding.FragmentHomeBinding
import com.app.movies.views.activities.MovieDetailActivity
import com.app.movies.views.adapters.MovieAdapter
import com.app.movies.views.viewModels.NetworkViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : BaseFragment() {

    var binding : FragmentHomeBinding ?= null

    private var resultsAdapter: MovieAdapter ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        afterDelay(500){
            networkViewModel.getVideos()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNetworkObserver()
    }
    private fun initNetworkObserver() {
        binding?.apply {
            lifecycleScope.launch {
                networkViewModel.videos.collect { responseState ->
                    when (responseState) {
                        is ResponseState.Loading -> {
                            loadingAnimation.beVisible()
                        }

                        is ResponseState.Error -> {
                            loadingAnimation.beVisible()
                            requireContext().showToast(getString(R.string.error_while_fetching_movies))
                        }

                        is ResponseState.Success -> {
                            loadingAnimation.beGone()
                            val resultsList = responseState.data
                            initRecycler(resultsList)
                        }
                    }
                }
            }
        }
    }

    private fun initRecycler(resultsList: List<ApiResponse.Results>) {
        binding?.apply {
            resultsAdapter = MovieAdapter(resultsList) { selectedMovie ->

                val intent = Intent(requireContext(), MovieDetailActivity::class.java)
                intent.putExtra(Constants.movieItem, selectedMovie)
                startActivity(intent)
            }
            moviesRecyclerview.layoutManager = LinearLayoutManager(requireContext())
            moviesRecyclerview.adapter = resultsAdapter
        }

    }

}