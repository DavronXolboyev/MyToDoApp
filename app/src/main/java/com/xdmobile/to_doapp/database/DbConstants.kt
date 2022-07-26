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

    object FinancialTransactions {
        val TABLE_NAME = "financial_transactions"
        val ID = "id"
        val CARD_NAME = "card_name"
        val CARD_NUMBER = "card_number"
        val USER_ID = "user_id"
        val CARD_BALANCE = "card_balance"
        val EVENT_COST = "event_cost"
        val CARD_TYPE = "card_type"
        val BALANCE_TYPE = "balance_type"
    }

    object Preference {
        val NAME = "login"
        val KEY_REGISTER = "is_registered"
        val KEY_USER_ID = "user_id"
        val KEY_EMAIL_OR_USENAME = "username_or_email"
    }


}