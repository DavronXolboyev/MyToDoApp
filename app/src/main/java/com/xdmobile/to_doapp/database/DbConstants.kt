package com.xdmobile.to_doapp.database

class DbConstants {

    companion object {
        val DATABASE_NAME = "ToDo.db"
        var VERSION_CODE = 1
    }

    object ToDoTable {
        val TABLE_NAME = "todo"
        val ID = "id"
        val INTENDED_WORK = "intended_work"
        val DATE = "date"
        val IS_FINISHED = "is_finished"
        val USER_ID = "user_id"
    }

    object User {
        val TABLE_NAME = "user"
        val ID = "user_id"
        val USERNAME = "username"
        val EMAIL = "user_email"
        val PASSWORD = "user_password"
    }

    object Preference {
        val NAME = "login"
        val KEY_REGISTER = "isRegistered"
        val KEY_USER = "user"
    }


}