package com.xdmobile.to_doapp.constants

import com.xdmobile.to_doapp.R
import com.xdmobile.to_doapp.model.CardStyle
import kotlin.random.Random

class CardBackground {
    private val cardStyles: ArrayList<CardStyle> = arrayListOf(
        CardStyle(1, R.drawable.gradient_1, R.color.gold),
        CardStyle(2, R.drawable.gradient_2, R.color.black),
        CardStyle(3, R.drawable.gradient_3, R.color.white),
        CardStyle(4, R.drawable.gradient_4, R.color.purple_500),
        CardStyle(5, R.drawable.gradient_5, R.color.white),
        CardStyle(6, R.drawable.gradient_6, R.color.stratos),
        CardStyle(7, R.drawable.gradient_7, R.color.white),
        CardStyle(8, R.drawable.gradient_8, R.color.white),
        CardStyle(9, R.drawable.gradient_9, R.color.white),
        CardStyle(10, R.drawable.gradient_10, R.color.white)
    )

    companion object

    fun getCardStyle(): CardStyle {
        val index = Random.nextInt(cardStyles.size)
        return cardStyles[index]
    }

    fun getCardStyleById(id: Int): CardStyle = cardStyles[id - 1]
}