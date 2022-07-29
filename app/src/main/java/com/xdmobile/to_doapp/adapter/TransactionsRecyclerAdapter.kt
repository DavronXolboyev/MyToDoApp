package com.xdmobile.to_doapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xdmobile.to_doapp.R
import com.xdmobile.to_doapp.constants.Tools
import com.xdmobile.to_doapp.model.FinTranModel

class TransactionsRecyclerAdapter(
    private val context: Context,
    private val transactionsList: MutableList<FinTranModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolderItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val eventNameTv = itemView.findViewById<TextView>(R.id.item_finance_event_name)
        private val eventCoinTv = itemView.findViewById<TextView>(R.id.item_finance_event_coin)
        private val eventDayTv = itemView.findViewById<TextView>(R.id.item_finance_event_time)
        private val eventIconImg = itemView.findViewById<ImageView>(R.id.item_finance_img)

        fun bind(position: Int){
            with(transactionsList[position]){
                eventNameTv.text = eventName
                eventCoinTv.text = eventCost.toString()
                eventDayTv.text = addedTime
                eventIconImg.setImageResource(
                    if (eventCost > 0)
                        R.drawable.ic_import
                else
                    R.drawable.ic_export
                )
            }
        }
    }

    inner class ViewHolderGroup(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val day = itemView.findViewById<TextView>(R.id.item_finance_day_text)

        fun bind(position: Int){
            day.text = transactionsList[position].addedTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return when (viewType) {
            Tools.VIEW_TYPE_GROUP -> {
                val view = inflater.inflate(R.layout.item_finance_day, parent, false)
                ViewHolderGroup(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.item_financial_transactions, parent, false)
                ViewHolderItem(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolderGroup){
            holder.bind(position)
        }else if (holder is ViewHolderItem){
            holder.bind(position)
        }
    }

    override fun getItemCount(): Int {
        return transactionsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return transactionsList[position].viewType
    }
}