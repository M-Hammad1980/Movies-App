package com.app.movies.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.movies.data.model.ApiResponse
import com.app.movies.databinding.ItemMovieBinding
import com.bumptech.glide.Glide

class MovieAdapter(private val resultsList: List<ApiResponse.Results>, private val itemClickListener : (ApiResponse.Results) -> Unit) :
    RecyclerView.Adapter<MovieAdapter.ResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMovieBinding.inflate(inflater, parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val result = resultsList[position]
        holder.bind(result)
    }

    override fun getItemCount(): Int {
        return resultsList.size
    }

    inner class ResultViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: ApiResponse.Results) {
            with(binding){

                val posterUrl = "https://image.tmdb.org/t/p/w185${result.posterPath}"

                Glide.with(posterImage.context)
                    .load(posterUrl)
                    .into(posterImage)
            }


            itemView.setOnClickListener {
                itemClickListener.invoke(result)
            }
        }
    }
}