package com.xdmobile.to_doapp.adapter

import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.taosif7.android.ringchartlib.RingChart
import com.taosif7.android.ringchartlib.models.RingChartData
import com.xdmobile.to_doapp.R
import com.xdmobile.to_doapp.model.CardModel

class CardViewPagerAdapter(
    private val context: Context,
    private val cardList: MutableList<CardModel>
) :
    RecyclerView.Adapter<CardViewPagerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ringChart = itemView.findViewById<RingChart>(R.id.item_ring_chart)
        private val cardName = itemView.findViewById<TextView>(R.id.item_ring_card_name)
        fun bind(position: Int) {
            val value = 1 - cardList[position].cardExpenses / cardList[position].cardBalance
            val data = listOf(
                RingChartData(
                    value,
                    context.resources.getColor(cardList[position].cardStyle.lineColor),
                    ""
                )
            )
            ringChart.apply {
                setLayoutMode(RingChart.renderMode.MODE_OVERLAP)
                setData(data)
                startAnimateLoading()
                stopAnimation(ringChart)
            }

            cardName.text = cardList[position].cardName

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_rings, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    private fun stopAnimation(ringChart: RingChart) {
        object : CountDownTimer(1500, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }
            override fun onFinish() {
                ringChart.stopAnimateLoading()
            }

        }.start()
    }
}