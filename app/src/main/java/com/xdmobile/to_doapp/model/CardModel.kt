package com.xdmobile.to_doapp.model

data class CardModel(
    val id: Int,
    val cardName: String,
    var cardBalance: Float,
    val cardNumber: String,
    val cardDate: String,
    val cardType: String,
    val userId: Int,
    val cardStyle: CardStyle,
    var cardExpenses: Float = 0f,
    var cardReceipts: Float = 0f
)
