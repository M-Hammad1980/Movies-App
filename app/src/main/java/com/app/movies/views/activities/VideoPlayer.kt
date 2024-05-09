package com.app.movies.views.activities

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.app.movies.data.model.ResponseState
import com.app.movies.data.utils.Constants
import com.app.movies.data.utils.afterDelay
import com.app.movies.databinding.ActivityVideoPlayerBinding
import com.app.movies.views.dialogs.ProgressDialog
import com.app.movies.views.viewModels.NetworkViewModel
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.HttpDataSource
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class VideoPlayer : AppCompatActivity() {
    lateinit var binding: ActivityVideoPlayerBinding
    val networkViewModel by viewModel<NetworkViewModel>()
    private var exoplayer: ExoPlayer? = null
    private val loadingDialog by lazy {
        ProgressDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataSourceFactory = DefaultHttpDataSource.Factory()




        exoplayer = ExoPlayer.Builder(this)
            .setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .build()

        binding.playerView.setShowNextButton(false)
        binding.playerView.setShowPreviousButton(false)
        val movieId = intent.getIntExtra(Constants.videoId,0)
        Log.e("tag**//", "onCreate: movieId  ${movieId}")
        lifecycleScope.launch {
            networkViewModel.videosLinks.collect{ responseState ->
                when(responseState){
                    is ResponseState.Loading-> {
                        loadingDialog.show()
                    }
                    is ResponseState.Error-> {
                        onBackPressed()
                    }
                    is ResponseState.Success-> {
                        Log.e("tag**//", "onCreate: ${responseState.data.videoResults.size}")
                        Log.e("tag**//", "onCreate:  ${responseState.data.videoResults[0].key}")

                        val url = "https://www.youtube.com/watch?v=${responseState.data.videoResults[0].key}"

                        initializePlayerMpd(url)


                    }
                }

            }
        }

        networkViewModel.getVideosLinks(movieId)

    }

    private fun initializePlayerMpd(url: String) {


        binding.playerView.player = exoplayer
        /*val dataSourceFactory = DefaultDataSourceFactory(
            this@VideoPlayer,
            "Exoplayer"
        )*/
        val dataSourceFactory: HttpDataSource.Factory = DefaultHttpDataSource.Factory()
        val hlsMediaSource = HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(url)))

        try {

            exoplayer?.setMediaSource(hlsMediaSource)

            exoplayer?.prepare()
            exoplayer?.play()



            exoplayer?.addListener(object : Player.Listener {

                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_READY) {
                        afterDelay(500) {
                            loadingDialog?.apply {
                                if (isShowing)
                                    dismiss()
                            }
                        }
                    }
                    if (state == Player.STATE_ENDED) {

                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    Log.e("videoPlayer", "onPlayerError: ${error.message}")
                    super.onPlayerError(error)
                    loadingDialog?.apply {
                        if (isShowing) {
                            dismiss()
                        }
                    }
                }
            })
        } catch (e: ExoPlaybackException) {
            Log.e("catch", "Error initializing ExoPlayer", e)
            loadingDialog?.apply {
                if (isShowing) {
                    dismiss()
                }
            }

        }
    }

}