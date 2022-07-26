package com.xdmobile.to_doapp.model

data class FinTranModel(
    val id: Int,
    val cardName: String,
    val cardNumber: String,
    val cardBalance: Float,
    val eventCost: Float,
    val cardType: String,
    val balanceType: String,
    val userId: Int
)