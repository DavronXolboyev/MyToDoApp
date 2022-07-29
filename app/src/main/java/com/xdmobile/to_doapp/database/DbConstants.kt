package com.xdmobile.to_doapp.database

class DbConstants {

    companion object {
        const val DATABASE_NAME = "ToDo.db"
        var VERSION_CODE = 1
        val CREATE_USER_TABLE = "CREATE TABLE ${User.TABLE_NAME}(" +
                "${User.ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${User.USERNAME} VARCHAR(50) NOT NULL UNIQUE," +
                "${User.EMAIL} VARCHAR(50) NOT NULL UNIQUE," +
                "${User.PASSWORD} VARCHAR(50) NOT NULL)"

        val CREATE_CARDS_TABLE = "CREATE TABLE ${Cards.TABLE_NAME}(" +
                "${Cards.ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${Cards.CARD_NUMBER} TEXT NOT NULL UNIQUE," +
                "${Cards.CARD_DATE} TEXT NOT NULL," +
                "${Cards.CARD_NAME} TEXT NOT NULL," +
                "${Cards.CARD_BALANCE} FLOAT NOT NULL," +
                "${Cards.CARD_TYPE} TEXT NOT NULL," +
                "${Cards.USER_ID} INTEGER NOT NULL," +
                "${Cards.CARD_STYLE_ID} INTEGER NOT NULL," +
                "${Cards.CARD_EXPENSES} FLOAT NOT NULL DEFAULT 0," +
                "FOREIGN KEY(${Cards.USER_ID}) REFERENCES ${User.TABLE_NAME}(${User.ID})" +
                ")"

        val CREATE_FINANCIAL_TRANSACTIONS_TABLE =
            "CREATE TABLE ${FinancialTransactions.TABLE_NAME}(" +
                    "${FinancialTransactions.ID} INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "${FinancialTransactions.EVENT_COST} FLOAT NOT NULL," +
                    "${FinancialTransactions.ADDED_TIME} DATE NOT NULL," +
                    "${FinancialTransactions.USER_ID} INTEGER," +
                    "${FinancialTransactions.CARD_ID} INTEGER," +
                    "${FinancialTransactions.EVENT_NAME} TEXT NOT NULL," +
                    "FOREIGN KEY(${FinancialTransactions.CARD_ID}) REFERENCES ${Cards.TABLE_NAME}(${Cards.ID})," +
                    "FOREIGN KEY(${FinancialTransactions.USER_ID}) REFERENCES ${User.TABLE_NAME}(${User.ID})" +
                    ")"

        val CREATE_TO_DO_TABLE =
            "CREATE TABLE ${ToDoTable.TABLE_NAME} (" +
                    "${ToDoTable.ID} INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "${ToDoTable.INTENDED_WORK} TEXT NOT NULL," +
                    "${ToDoTable.DATE} DATE NOT NULL," +
                    "${ToDoTable.IS_FINISHED} INTEGER NOT NULL DEFAULT 0," +
                    "${ToDoTable.USER_ID} INTEGER," +
                    "FOREIGN KEY(${ToDoTable.USER_ID}) REFERENCES ${User.TABLE_NAME}(${User.ID})" +
                    ")"
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
        val USER_ID = "user_id"
        val EVENT_COST = "event_cost"
        val BALANCE_TYPE = "balance_type"
        val ADDED_TIME = "added_time"
        val CARD_ID = "card_id"
        val EVENT_NAME = "event_name"
    }

    object Cards {
        val TABLE_NAME = "cards"
        val ID = "id"
        val CARD_NUMBER = "card_number"
        val CARD_NAME = "card_name"
        val CARD_DATE = "card_date"
        val CARD_BALANCE = "card_balance"
        val CARD_TYPE = "card_type"
        val USER_ID = "user_id"
        val CARD_STYLE_ID = "card_style_id"
        val CARD_EXPENSES = "card_expenses"
    }

    object Preference {
        val NAME = "login"
        val KEY_REGISTER = "is_registered"
        val KEY_USER_ID = "user_id"
        val KEY_EMAIL_OR_USENAME = "username_or_email"
    }


}