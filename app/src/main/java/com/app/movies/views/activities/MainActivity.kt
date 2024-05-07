package com.app.movies.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.app.movies.data.model.ResponseState
import com.app.movies.data.utils.afterDelay
import com.app.movies.databinding.ActivityMainBinding
import com.app.movies.views.viewModels.NetworkViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    val networkViewModel by viewModel<NetworkViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        lifecycleScope.launch {
            networkViewModel.videos.collect{responseState ->
                when(responseState)
                {
                    is ResponseState.Loading -> {

                    }

                    is ResponseState.Error -> {

                    }

                    is ResponseState.Success -> {
                        Log.e("tag**", "activity: ${responseState.data}")
                        Log.e("tag**", "activity title: ${responseState.data.results[0].originalTitle}")

                    }
                }


            }
        }
        afterDelay(2000){
            networkViewModel.getVideos()
        }
    }
}