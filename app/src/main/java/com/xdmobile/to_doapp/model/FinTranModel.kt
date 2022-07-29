package com.xdmobile.to_doapp.model

data class FinTranModel(
    val id: Int,
    val eventCost: Float,
    val userId: Int,
    val addedTime: String,
    val cardId: Int,
    var viewType : Int,
    val eventName : String,
)