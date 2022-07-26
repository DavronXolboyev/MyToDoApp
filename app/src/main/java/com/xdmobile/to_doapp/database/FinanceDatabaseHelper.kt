package com.xdmobile.to_doapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.xdmobile.to_doapp.database.DbConstants.FinancialTransactions
import com.xdmobile.to_doapp.database.DbConstants.User

class FinanceDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DbConstants.DATABASE_NAME, null, DbConstants.VERSION_CODE) {

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE ${FinancialTransactions.TABLE_NAME}(" +
                "${FinancialTransactions.ID} INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "${FinancialTransactions.CARD_NAME} TEXT NOT NULL," +
                "${FinancialTransactions.CARD_NUMBER} TEXT NOT NULL," +
                "${FinancialTransactions.CARD_TYPE} TEXT NOT NULL," +
                "${FinancialTransactions.BALANCE_TYPE} TEXT NOT NULL," +
                "${FinancialTransactions.CARD_BALANCE} FLOAT NOT NULL," +
                "${FinancialTransactions.EVENT_COST} FLOAT NOT NULL," +
                "${FinancialTransactions.USER_ID} INTEGER," +
                "FOREIGN KEY(${FinancialTransactions.USER_ID}) REFERENCES ${User.TABLE_NAME}(${User.ID})" +
                ")"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTable = "DROP TABLE IF EXISTS ${FinancialTransactions.TABLE_NAME}"
        db?.execSQL(dropTable)
        onCreate(db)
    }

    fun insertData(){}
}