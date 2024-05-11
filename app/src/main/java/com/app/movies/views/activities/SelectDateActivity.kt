package com.app.movies.views.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.children
import com.app.movies.R
import com.app.movies.data.model.ApiResponse
import com.app.movies.data.utils.Constants
import com.app.movies.data.utils.addConsecutiveDates
import com.app.movies.databinding.ActivitySelectDateBinding
import com.app.movies.views.adapters.DateAdapter
import com.google.android.material.chip.Chip

class SelectDateActivity : AppCompatActivity() {
    lateinit var binding : ActivitySelectDateBinding
    private var adapter : DateAdapter ?= null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectDateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val selectedMovie = intent.getParcelableExtra<ApiResponse.Results>(Constants.movieItem)

        with(binding){
            toolbar.toolbarTitle.text = selectedMovie?.originalTitle
            toolbar.toolbarDate.text = "In Theatres ${selectedMovie?.releaseDate}"
            toolbar.back.setOnClickListener {
                onBackPressed()
            }
            constraintLayout.setOnClickListener {
                onLayoutClicked(it)
            }
            constraintLayout1.setOnClickListener {
                onLayoutClicked(it)
            }
            constraintLayout2.setOnClickListener {
                onLayoutClicked(it)
            }
        }


        selectedMovie?.releaseDate?.let {

            val consecutiveDates = addConsecutiveDates(it,8)
            adapter = DateAdapter(consecutiveDates)
            binding.recyclerView.adapter = adapter
        }
    }

    fun onLayoutClicked(view: View) {
        with(binding){
            constraintLayout.setBackgroundResource(R.drawable.rounded_grey)
            constraintLayout1.setBackgroundResource(R.drawable.rounded_grey)
            constraintLayout2.setBackgroundResource(R.drawable.rounded_grey)
            view.setBackgroundResource(R.drawable.rounded_blue)
        }
    }
}