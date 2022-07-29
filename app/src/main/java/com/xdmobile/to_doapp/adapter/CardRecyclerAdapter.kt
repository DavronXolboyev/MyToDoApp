package com.xdmobile.to_doapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xdmobile.to_doapp.R
import com.xdmobile.to_doapp.model.CardModel
import java.lang.StringBuilder

class CardRecyclerAdapter(
    private val context: Context,
    private val cardList: MutableList<CardModel>
) :

    RecyclerView.Adapter<CardRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardNumber = itemView.findViewById<TextView>(R.id.item_card_number)
        private val cardName = itemView.findViewById<TextView>(R.id.item_card_name)
        private val cardBalance = itemView.findViewById<TextView>(R.id.item_card_balance)
        private val cardDate = itemView.findViewById<TextView>(R.id.item_card_date)
        private val cardIcon = itemView.findViewById<ImageView>(R.id.item_card_icon)
        private val card = itemView.findViewById<RelativeLayout>(R.id.item_card_layout)

        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            cardNumber.text = getFormattedCardNumber(cardList[position].cardNumber)
            cardNumber.setTextColor(context.resources.getColor(cardList[position].cardStyle.textColor))

            cardName.text = cardList[position].cardName
            cardName.setTextColor(context.resources.getColor(cardList[position].cardStyle.textColor))
            val balance = cardList[position].cardBalance - cardList[position].cardExpenses
            cardBalance.text =
                "${getFormattedCardBalance(balance)} UZS"
            cardBalance.setTextColor(context.resources.getColor(cardList[position].cardStyle.textColor))

            cardDate.text = cardList[position].cardDate
            cardDate.setTextColor(context.resources.getColor(cardList[position].cardStyle.textColor))

            cardIcon.setImageResource(
                if (cardList[position].cardType == "HUMO")
                    R.drawable.humo1
                else
                    R.drawable.uzcard1
            )
            card.setBackgroundResource(cardList[position].cardStyle.gradient)
        }
    }

    private fun getFormattedCardBalance(balance: Float): String {
        Log.i("TAG", "getFormattedCardBalance: $balance")
        val s = balance.toLong().toString()
        val builder = StringBuilder()
        var counter = 0
        for (i in s.lastIndex downTo 0) {
            builder.append(s[i])
            counter++
            if (counter % 3 == 0 && counter != s.length)
                builder.append(" ")
        }
        return builder.toString().reversed()
    }

    private fun getFormattedCardNumber(cardNumber: String): CharSequence {
        return "${cardNumber.substring(0, 4)} " +
                "${cardNumber.substring(4, 8)} " +
                "${cardNumber.substring(8, 12)} " +
                cardNumber.substring(12)
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