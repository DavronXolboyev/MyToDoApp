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
import com.xdmobile.to_doapp.model.CardModel

class CardRecyclerAdapter(
    private val context: Context,
    private val cardList: MutableList<CardModel>
) :

    RecyclerView.Adapter<CardRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardNumber = itemView.findViewById<TextView>(R.id.item_card_number)
        val cardName = itemView.findViewById<TextView>(R.id.item_card_name)
        val cardBalance = itemView.findViewById<TextView>(R.id.item_card_balance)
        val cardDate = itemView.findViewById<TextView>(R.id.item_card_date)
        val cardIcon = itemView.findViewById<ImageView>(R.id.item_card_icon)
        val card = itemView.findViewById<RelativeLayout>(R.id.item_card_layout)
        fun bind(position: Int) {
            cardNumber.text = cardList[position].cardNumber
            cardNumber.setTextColor(context.resources.getColor(cardList[position].cardStyle.textColor))

            cardName.text = cardList[position].cardName
            cardName.setTextColor(context.resources.getColor(cardList[position].cardStyle.textColor))

            cardBalance.text = cardList[position].cardBalance.toString()
            cardBalance.setTextColor(context.resources.getColor(cardList[position].cardStyle.textColor))

            cardDate.text = cardList[position].cardDate
            cardDate.setTextColor(context.resources.getColor(cardList[position].cardStyle.textColor))

            cardIcon.setImageResource(
                if (cardList[position].cardNumber.startsWith("9860"))
                    R.drawable.icon_humo
                else
                    R.drawable.icon_uzcard
            )
            card.setBackgroundResource(cardList[position].cardStyle.gradient)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_card, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return cardList.size
    }
}