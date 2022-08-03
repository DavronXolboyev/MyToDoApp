package com.xdmobile.to_doapp.database

class DbConstants {

    companion object {
        const val DATABASE_NAME = "ToDo.db"
        var VERSION_CODE = 1
        const val CREATE_USER_TABLE = "CREATE TABLE ${User.TABLE_NAME}(" +
                "${User.ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${User.USERNAME} VARCHAR(50) NOT NULL UNIQUE," +
                "${User.EMAIL} VARCHAR(50) NOT NULL UNIQUE," +
                "${User.PASSWORD} VARCHAR(50) NOT NULL)"

        const val CREATE_CARDS_TABLE = "CREATE TABLE ${Cards.TABLE_NAME}(" +
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

        const val CREATE_FINANCIAL_TRANSACTIONS_TABLE =
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

        const val CREATE_TO_DO_TABLE =
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
        const val TABLE_NAME = "todo"
        const val ID = "id"
        const val INTENDED_WORK = "intended_work"
        const val DATE = "date"
        const val IS_FINISHED = "is_finished"
        const val USER_ID = "user_id"
    }

    object User {
        const val TABLE_NAME = "user"
        const val ID = "user_id"
        const val USERNAME = "username"
        const val EMAIL = "user_email"
        const val PASSWORD = "user_password"
    }

    object FinancialTransactions {
        const val TABLE_NAME = "financial_transactions"
        const val ID = "id"
        const val USER_ID = "user_id"
        const val EVENT_COST = "event_cost"
        const val BALANCE_TYPE = "balance_type"
        const val ADDED_TIME = "added_time"
        const val CARD_ID = "card_id"
        const val EVENT_NAME = "event_name"
    }

    object Cards {
        const val TABLE_NAME = "cards"
        const val ID = "id"
        const val CARD_NUMBER = "card_number"
        const val CARD_NAME = "card_name"
        const val CARD_DATE = "card_date"
        const val CARD_BALANCE = "card_balance"
        const val CARD_TYPE = "card_type"
        const val USER_ID = "user_id"
        const val CARD_STYLE_ID = "card_style_id"
        const val CARD_EXPENSES = "card_expenses"
    }

    object Preference {
        const val NAME = "login"
        const val KEY_REGISTER = "is_registered"
        const val KEY_USER_ID = "user_id"
        const val KEY_EMAIL_OR_USERNAME = "username_or_email"
        const val KEY_CARD_ID = "card_id"
    }


}