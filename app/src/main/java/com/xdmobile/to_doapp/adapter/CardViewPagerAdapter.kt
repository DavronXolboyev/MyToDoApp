package com.xdmobile.to_doapp.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
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
        private val pieChart = itemView.findViewById<PieChart>(R.id.item_pie_chart)
        fun bind(position: Int) {
            val percentageReceipts =
                cardList[position].cardReceipts / (cardList[position].cardReceipts + cardList[position].cardExpenses)
            val percentageExpenses = 1 - percentageReceipts
            Log.i("TAG", "bind: ${percentageExpenses + percentageReceipts}")

            val entries = listOf(
                PieEntry(percentageExpenses, "Expenses"),
                PieEntry(percentageReceipts, "Receipts")
            )

            val dataSet = PieDataSet(entries, cardList[position].cardName).apply {
                colors = listOf(
                    context.resources.getColor(cardList[position].cardStyle.lineColor1),
                    context.resources.getColor(cardList[position].cardStyle.lineColor2)
                )
                valueTextColor = Color.WHITE
            }

            val data = PieData(dataSet).apply {
                setDrawValues(true)
                setValueFormatter(PercentFormatter(pieChart))
                setValueTextSize(14f)
                setValueTypeface(Typeface.DEFAULT_BOLD)
                setValueTextColor(Color.WHITE)
            }

            val desc = Description()
            desc.text = ""

            pieChart.apply {
                description = desc
                this.data = data
                setEntryLabelColor(Color.WHITE)
                setEntryLabelTextSize(16f)
                centerText = cardList[position].cardName
                setCenterTextSize(20f)
                setHoleColor(Color.TRANSPARENT)
                animateY(1500, Easing.EaseInOutQuad)
                holeRadius = 65f
                setCenterTextTypeface(Typeface.DEFAULT_BOLD)
                invalidate()
            }
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

}