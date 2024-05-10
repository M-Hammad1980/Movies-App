package com.app.movies.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.movies.data.model.ApiResponse
import com.app.movies.data.utils.Constants
import com.app.movies.databinding.ItemMovieBinding
import com.bumptech.glide.Glide
import androidx.recyclerview.widget.DiffUtil
import com.app.movies.databinding.ItemSearchMovieBinding

class SearchMovieAdapter(private val itemClickListener: (ApiResponse.Results) -> Unit) :
    RecyclerView.Adapter<SearchMovieAdapter.ResultViewHolder>() {

    private var resultsList = listOf<ApiResponse.Results>()

    // Update the adapter data list with the new list
    fun updateList(newList: List<ApiResponse.Results>) {
        val diffResult = DiffUtil.calculateDiff(SearchDiffCallback(resultsList, newList))
        resultsList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSearchMovieBinding.inflate(inflater, parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val result = resultsList[position]
        holder.bind(result)
    }

    override fun getItemCount(): Int {
        return resultsList.size
    }

    inner class ResultViewHolder(private val binding: ItemSearchMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: ApiResponse.Results) {
            with(binding) {
                title.text = result.originalTitle
                overView.text = result.overview
                val posterUrl = "${Constants.imageFormatUrl}${result.posterPath}"

                Glide.with(posterImage.context)
                    .load(posterUrl)
                    .into(posterImage)
            }

            itemView.setOnClickListener {
                itemClickListener.invoke(result)
            }
        }
    }

    // DiffUtil Callback subclass to calculate the differences between two lists
    class SearchDiffCallback(private val oldList: List<ApiResponse.Results>, private val newList: List<ApiResponse.Results>) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}