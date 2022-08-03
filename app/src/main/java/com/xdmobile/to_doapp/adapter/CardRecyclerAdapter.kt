package com.xdmobile.to_doapp.adapter

import android.annotation.SuppressLint
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
import com.xdmobile.to_doapp.interfaces.CardOnLongClickListener
import com.xdmobile.to_doapp.model.CardModel

class CardRecyclerAdapter(
    private val context: Context,
    private val cardList: MutableList<CardModel>
) :

    RecyclerView.Adapter<CardRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val cardNumber = itemView.findViewById<TextView>(R.id.item_card_number)
        private val cardName = itemView.findViewById<TextView>(R.id.item_card_name)
        private val cardBalance = itemView.findViewById<TextView>(R.id.item_card_balance)
        private val cardDate = itemView.findViewById<TextView>(R.id.item_card_date)
        private val cardIcon = itemView.findViewById<ImageView>(R.id.item_card_icon)
        private val card = itemView.findViewById<RelativeLayout>(R.id.item_card_layout)

        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            cardNumber.text = Tools.getFormattedCardNumber(cardList[position].cardNumber)
            cardNumber.setTextColor(context.resources.getColor(cardList[position].cardStyle.textColor))

            cardName.text = cardList[position].cardName
            cardName.setTextColor(context.resources.getColor(cardList[position].cardStyle.textColor))
            cardBalance.text =
                "${Tools.getFormattedCardBalance(cardList[position].cardBalance)} UZS"
            cardBalance.setTextColor(context.resources.getColor(cardList[position].cardStyle.textColor))

            cardDate.text = cardList[position].cardDate
            cardDate.setTextColor(context.resources.getColor(cardList[position].cardStyle.textColor))

            cardIcon.setImageResource(Tools.getCardIcon(cardList[position].cardType))

            card.setBackgroundResource(cardList[position].cardStyle.gradient)
            itemView.setOnLongClickListener {
                if (position != RecyclerView.NO_POSITION)
                    onLongClickListener?.onLongClick(adapterPosition, itemView)
                false
            }
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

    private var onLongClickListener: CardOnLongClickListener? = null

    fun setOnLongClickListener(listener: CardOnLongClickListener) {
        onLongClickListener = listener
    }

}