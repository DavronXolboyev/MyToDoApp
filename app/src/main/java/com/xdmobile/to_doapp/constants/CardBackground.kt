package com.xdmobile.to_doapp.constants

import com.xdmobile.to_doapp.R
import com.xdmobile.to_doapp.model.CardStyle
import kotlin.random.Random

class CardBackground {
    private val cardStyles: ArrayList<CardStyle> = arrayListOf(
        CardStyle(1, R.drawable.gradient_1, R.color.gold, R.color.text_color,R.color.twitter_button_color),
        CardStyle(2, R.drawable.gradient_2, R.color.black, R.color.stratos,R.color.orange),
        CardStyle(3, R.drawable.gradient_3, R.color.white, R.color.gold,R.color.pink),
        CardStyle(4, R.drawable.gradient_4, R.color.purple_500, R.color.green_light,R.color.text_color),
        CardStyle(5, R.drawable.gradient_5, R.color.white, R.color.red,R.color.gold),
        CardStyle(6, R.drawable.gradient_6, R.color.stratos, R.color.twitter_button_color,R.color.black),
        CardStyle(7, R.drawable.gradient_7, R.color.white, R.color.orange,R.color.text_color),
        CardStyle(8, R.drawable.gradient_8, R.color.white, R.color.pink,R.color.green),
        CardStyle(9, R.drawable.gradient_9, R.color.white, R.color.purple,R.color.dark_red),
        CardStyle(10, R.drawable.gradient_10, R.color.white, R.color.blue,R.color.text_color)
    )

    fun getCardStyle(): CardStyle {
        val rand = java.util.Random()
        val index = rand.nextInt(cardStyles.size)
        return cardStyles[index]
    }

    fun getCardStyleById(id: Int): CardStyle = cardStyles[id - 1]
}