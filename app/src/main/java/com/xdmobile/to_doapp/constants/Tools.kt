package com.xdmobile.to_doapp.constants

import android.annotation.SuppressLint
import android.util.Log
import com.xdmobile.to_doapp.R
import com.xdmobile.to_doapp.model.FinTranModel
import java.lang.StringBuilder
import java.time.LocalDate

class Tools {
    companion object {
        const val VIEW_TYPE_GROUP = 0
        const val VIEW_TYPE_ITEM = 1

        @SuppressLint("NewApi")
        fun getTransactionsWithType(transactionsList: MutableList<FinTranModel>): MutableList<FinTranModel> {
            val currentDay = LocalDate.now().toString()
            val newList = mutableListOf<FinTranModel>()
            val finTranModel = transactionsList[0]

            if (finTranModel.addedTime == currentDay) {
                newList.add(FinTranModel(0, 0f, 0, "Today", 0, VIEW_TYPE_GROUP, ""))
            } else {
                finTranModel.viewType = VIEW_TYPE_GROUP
                newList.add(finTranModel)
            }

            for (i in 0 until transactionsList.lastIndex) {
                val date1 = transactionsList[i].addedTime
                val date2 = transactionsList[i + 1].addedTime
                if (date1 == date2) {
                    transactionsList[i].viewType = VIEW_TYPE_ITEM
                    newList.add(transactionsList[i])
                } else {
                    transactionsList[i].viewType = VIEW_TYPE_ITEM
                    newList.add(transactionsList[i])
                    newList.add(FinTranModel(0, 0f, 0, date2, 0, VIEW_TYPE_GROUP, ""))
                }
            }

            transactionsList[transactionsList.lastIndex].viewType = VIEW_TYPE_ITEM
            newList.add(transactionsList[transactionsList.lastIndex])



            return newList
        }

        fun getFormattedCardBalance(balance: Float): String {
//            Log.i("TAG", "getFormattedCardBalance: $balance")
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

        fun getFormattedCardNumber(cardNumber: String): CharSequence {
            return "${cardNumber.substring(0, 4)} " +
                    "${cardNumber.substring(4, 8)} " +
                    "${cardNumber.substring(8, 12)} " +
                    cardNumber.substring(12)
        }

        fun getCardIcon(cardType: String) = if (cardType == "HUMO")
            R.drawable.humo1
        else
            R.drawable.uzcard1
    }


}