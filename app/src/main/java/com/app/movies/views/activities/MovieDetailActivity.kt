package com.app.movies.views.activities

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.app.movies.R
import com.app.movies.data.model.ApiResponse
import com.app.movies.data.utils.Constants
import com.app.movies.data.utils.addConsecutiveDates
import com.app.movies.data.utils.getGenresById
import com.app.movies.databinding.ActivityMovieDetailBinding
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class MovieDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityMovieDetailBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val selectedMovie = intent.getParcelableExtra<ApiResponse.Results>(Constants.movieItem)



        val genres = selectedMovie?.genreIds?.let { getGenresById(it) } // Example IDs
        val chipGroup = findViewById<ChipGroup>(R.id.chipGroup)

        genres?.forEach { genre ->
            val chip = Chip(this)
            chip.text = genre
            chip.setTextColor(Color.BLACK)
//            chip.setChipBackgroundColorResource(getRandomColor())
            chip.isClickable = true
            chipGroup.addView(chip)
        }

        with(binding){
            titleText.text = "In Theatres ${selectedMovie?.releaseDate}"

            descriptionText.text = selectedMovie?.overview

            val coverUrl = "${Constants.imageFormatUrl}${selectedMovie?.posterPath}"
            Glide.with(this@MovieDetailActivity)
                .load(coverUrl)
                .into(coverImage)

            back.setOnClickListener {
                onBackPressed()
            }
            getTicket.setOnClickListener {
                val intent = Intent(this@MovieDetailActivity, SelectDateActivity::class.java)
                intent.putExtra(Constants.movieItem, selectedMovie)
                startActivity(intent)
            }
            watchTrailer.setOnClickListener {
                val intent = Intent(this@MovieDetailActivity,VideoPlayerYoutube::class.java)
                intent.putExtra(Constants.videoId,selectedMovie?.id)
                startActivity(intent)
            }

        }


    }


}