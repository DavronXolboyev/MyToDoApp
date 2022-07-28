package com.xdmobile.to_doapp.model

data class CardModel(
    val id: Int,
    val cardName: String,
    val cardBalance: Float,
    val cardNumber: String,
    val cardDate: String,
    val cardType: String,
    val userId : Int,
    val cardStyle : CardStyle
)
