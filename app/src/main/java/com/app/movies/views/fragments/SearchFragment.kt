package com.app.movies.views.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.app.movies.data.utils.beGone
import com.app.movies.data.utils.beVisible
import com.app.movies.databinding.FragmentSearchBinding
import com.app.movies.views.activities.MovieDetailActivity
import com.app.movies.views.adapters.MovieAdapter
import com.app.movies.views.adapters.SearchMovieAdapter
import kotlinx.coroutines.launch

class SearchFragment : BaseFragment() {
    var binding : FragmentSearchBinding ?= null
    private var searchAdapter: SearchMovieAdapter?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initNetworkObserver()

        binding?.apply {
            imgCancel.setOnClickListener{
                edtSearchBar.text.clear()
                edtSearchBar.clearFocus()
            }
            edtSearchBar.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (s.toString().isNotEmpty())
                        networkViewModel.getSearchVideos(s.toString())
                }
            })
        }
    }

    private fun initNetworkObserver() {
        binding?.apply {
            lifecycleScope.launch {
                networkViewModel.searchedVideos.collect { responseState ->
                    when (responseState) {
                        is ResponseState.Loading -> {

                        }

                        is ResponseState.Error -> {
                        }

                        is ResponseState.Success -> {
                            val resultsList = responseState.data.results
                            searchAdapter?.updateList(resultsList)
                        }
                    }
                }
            }
        }
    }

    private fun initRecycler() {
        binding?.apply {
            searchAdapter = SearchMovieAdapter { selectedMovie ->
                val intent = Intent(requireContext(), MovieDetailActivity::class.java)
                intent.putExtra(Constants.movieItem, selectedMovie)
                startActivity(intent)
            }
            recyclerSearchedMovies.adapter = searchAdapter
        }

    }


}