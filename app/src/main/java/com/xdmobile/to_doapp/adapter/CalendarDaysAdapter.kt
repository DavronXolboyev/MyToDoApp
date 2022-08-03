package com.xdmobile.to_doapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xdmobile.to_doapp.R

class CalendarDaysAdapter(private val context: Context, val daysList: ArrayList<String>) :
    RecyclerView.Adapter<CalendarDaysAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val datTv = itemView.findViewById<TextView>(R.id.item_day_tv)
        fun bind(position: Int) {
            if (position != RecyclerView.NO_POSITION)
                datTv.text = daysList[position]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_calendar_day, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return daysList.size
    }
}