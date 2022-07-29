package com.xdmobile.to_doapp.constants

import android.annotation.SuppressLint
import com.xdmobile.to_doapp.model.FinTranModel
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
                newList.add(FinTranModel(0, 0f, 0, "Today", 0, VIEW_TYPE_GROUP,""))
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
                    newList.add(FinTranModel(0, 0f, 0, date2, 0, VIEW_TYPE_GROUP,""))
                }
            }

            transactionsList[transactionsList.lastIndex].viewType = VIEW_TYPE_ITEM
            newList.add(transactionsList[transactionsList.lastIndex])



            return newList
        }
    }
}