package com.app.movies.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.movies.R

class DateAdapter(private val dataList: List<String>) :
    RecyclerView.Adapter<DateAdapter.ViewHolder>() {

    var selectedItemPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chp, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item, position)
    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.textView)

        init {
            textView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    selectedItemPosition = position
                    notifyDataSetChanged()
                }
            }
        }

        fun bind(item: String, position: Int) {
            textView.text = item
            if (position == selectedItemPosition) {
                textView.setBackgroundResource(R.drawable.rounded_blue_max)
                textView.setTextColor(itemView.context.resources.getColor(android.R.color.white))
            } else {
                textView.setBackgroundResource(R.drawable.rounded_white_max)
                textView.setTextColor(itemView.context.resources.getColor(android.R.color.black))
            }
        }
    }
}
