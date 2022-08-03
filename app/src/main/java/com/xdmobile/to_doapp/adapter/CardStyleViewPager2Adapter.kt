package com.xdmobile.to_doapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xdmobile.to_doapp.R
import com.xdmobile.to_doapp.constants.Tools
import com.xdmobile.to_doapp.model.CardModel

class CardStyleViewPager2Adapter(private val context : Context,private val cards: MutableList<CardModel>) :
    RecyclerView.Adapter<CardStyleViewPager2Adapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardNumber = itemView.findViewById<TextView>(R.id.v_item_card_number)
        private val cardName = itemView.findViewById<TextView>(R.id.v_item_card_name)
        private val cardBalance = itemView.findViewById<TextView>(R.id.v_item_card_balance)
        private val cardDate = itemView.findViewById<TextView>(R.id.v_item_card_date)
        private val cardIcon = itemView.findViewById<ImageView>(R.id.v_item_card_icon)
        private val card = itemView.findViewById<RelativeLayout>(R.id.v_item_card_layout)
        fun bind(position: Int) {
            cardNumber.text = Tools.getFormattedCardNumber(cards[adapterPosition].cardNumber)
            cardNumber.setTextColor(context.resources.getColor(cards[position].cardStyle.textColor))

            cardName.text = cards[adapterPosition].cardName
            cardName.setTextColor(context.resources.getColor(cards[position].cardStyle.textColor))

            cardBalance.text = Tools.getFormattedCardBalance(cards[adapterPosition].cardBalance)
            cardBalance.setTextColor(context.resources.getColor(cards[position].cardStyle.textColor))

            cardDate.text = cards[adapterPosition].cardDate
            cardDate.setTextColor(context.resources.getColor(cards[position].cardStyle.textColor))

            cardIcon.setImageResource(R.drawable.uzcard1)
            card.setBackgroundResource(cards[position].cardStyle.gradient)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_card_v, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return cards.size
    }
}