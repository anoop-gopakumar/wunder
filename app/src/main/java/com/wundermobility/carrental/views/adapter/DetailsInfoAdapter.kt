package com.wundermobility.carrental.views.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wundermobility.carrental.R

class DetailsInfoAdapter(private val dataSet: ArrayList<Pair<String,String>>) : RecyclerView.Adapter<DetailsInfoAdapter.DetailsInfoViewHolder>() {

    class DetailsInfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val key: TextView = view.findViewById(R.id.key)
        val value: TextView = view.findViewById(R.id.value)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): DetailsInfoViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item, viewGroup, false)

        return DetailsInfoViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: DetailsInfoViewHolder, position: Int) {

        viewHolder.key.text = dataSet[position].first
        viewHolder.value.text = dataSet[position].second
    }

    override fun getItemCount() = dataSet.size

}