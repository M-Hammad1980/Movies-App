package com.app.movies.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.app.movies.R
import com.app.movies.data.model.ApiResponse
import com.app.movies.data.utils.Constants
import com.app.movies.data.utils.beVisible
import com.app.movies.databinding.ActivityMovieDetailBinding
import com.bumptech.glide.Glide

class MovieDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityMovieDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val selectedMovie = intent.getParcelableExtra<ApiResponse.Results>(Constants.movieItem)

        with(binding){
            toolbarLayout.back.beVisible()
            toolbarLayout.toolbarTitle.text = getString(R.string.movie_details)
            titleText.text = selectedMovie?.originalTitle
            rating.text = selectedMovie?.voteAverage.toString()
            descriptionText.text = selectedMovie?.overview

            val posterUrl = "https://image.tmdb.org/t/p/w185${selectedMovie?.posterPath}"
            Glide.with(this@MovieDetailActivity)
                .load(posterUrl)
                .into(posterImageSmall)

            val coverUrl = "https://image.tmdb.org/t/p/w185${selectedMovie?.backdropPath}"
            Glide.with(this@MovieDetailActivity)
                .load(coverUrl)
                .into(coverImage)

            toolbarLayout.back.setOnClickListener {
                onBackPressed()
            }
            watchTrailer.setOnClickListener {
                val intent = Intent(this@MovieDetailActivity,VideoPlayer::class.java)
                intent.putExtra(Constants.videoId,selectedMovie?.id)
                startActivity(intent)
            }

        }


    }


}