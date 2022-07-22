package com.xdmobile.to_doapp.model

data class ToDoModel(
    val id: Int,
    val intendedWork: String,
    val date: String,
    val isFinished: Boolean
)