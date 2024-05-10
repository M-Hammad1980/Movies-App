package com.app.movies.views.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.app.movies.views.viewModels.NetworkViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

open class BaseFragment : Fragment() {
    protected val networkViewModel by viewModel<NetworkViewModel>()
}