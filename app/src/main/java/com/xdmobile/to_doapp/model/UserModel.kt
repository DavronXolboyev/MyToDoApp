package com.xdmobile.to_doapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
) : Parcelable {
    constructor(username: String, email: String, password: String) : this(0,
        username,
        email,
        password)
}