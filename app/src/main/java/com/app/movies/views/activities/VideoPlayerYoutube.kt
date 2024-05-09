package com.app.movies.views.activities

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.app.movies.data.model.ResponseState
import com.app.movies.data.utils.Constants
import com.app.movies.databinding.ActivityYoutubePlayerBinding
import com.app.movies.views.dialogs.ProgressDialog
import com.app.movies.views.viewModels.NetworkViewModel
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class VideoPlayerYoutube : AppCompatActivity() {
    lateinit var binding: ActivityYoutubePlayerBinding
    val networkViewModel by viewModel<NetworkViewModel>()
    private val loadingDialog by lazy {
        ProgressDialog(this)
    }


    private var fullscreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYoutubePlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycle.addObserver(binding.youtubePlayerView)

        val movieId = intent.getIntExtra(Constants.videoId, 0)
        Log.e("tag**//", "onCreate: movieId  ${movieId}")
        initNetworkObserver()
        networkViewModel.getVideosLinks(movieId)

        with(binding){
            imgBackBtn.setOnClickListener {
                onBackPressed()
            }
            imgOrientation.setOnClickListener {
                toggleFullscreen()
            }
        }
    }

    private fun initNetworkObserver() {
        lifecycleScope.launch {
            networkViewModel.videosLinks.collect { responseState ->
                when (responseState) {
                    is ResponseState.Loading -> {
                        loadingDialog.show()
                    }

                    is ResponseState.Error -> {
                        onBackPressed()
                    }

                    is ResponseState.Success -> {
                        Log.e("tag**//", "onCreate: ${responseState.data.videoResults.size}")
                        Log.e("tag**//", "onCreate:  ${responseState.data.videoResults[0].key}")

                        val url =
                            "https://www.youtube.com/watch?v=${responseState.data.videoResults[0].key}"
                        val videoId = responseState.data.videoResults[0].key
                        initPlayer(videoId)
                    }
                }

            }
        }
    }

    private fun initPlayer(videoId: String?) {
        binding.youtubePlayerView.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                if (videoId != null) {
                    if (loadingDialog.isShowing)
                        loadingDialog.dismiss()
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                super.onStateChange(youTubePlayer, state)
                if (state == PlayerConstants.PlayerState.ENDED) {
                    this@VideoPlayerYoutube.finish()
                }
            }
        })
    }

    private fun toggleFullscreen() {
        if (fullscreen) {
            exitFullscreen()
        } else {
            enterFullscreen()
        }
    }

    private fun enterFullscreen() {
        supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val params = binding.youtubePlayerView.layoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.youtubePlayerView.layoutParams = params

        fullscreen = true
    }

    private fun exitFullscreen() {
        supportActionBar?.show()
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val params = binding.youtubePlayerView.layoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        binding.youtubePlayerView.layoutParams = params

        fullscreen = false
    }

}