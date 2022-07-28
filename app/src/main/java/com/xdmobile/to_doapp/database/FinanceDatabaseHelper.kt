package com.xdmobile.to_doapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.xdmobile.to_doapp.database.DbConstants.*
import com.xdmobile.to_doapp.model.FinTranModel

class FinanceDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DbConstants.DATABASE_NAME, null, DbConstants.VERSION_CODE) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DbConstants.CREATE_FINANCIAL_TRANSACTIONS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTable = "DROP TABLE IF EXISTS ${FinancialTransactions.TABLE_NAME}"
        db?.execSQL(dropTable)
        onCreate(db)
    }

    fun insertData(finTranModel: FinTranModel): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            with(finTranModel) {
                put(FinancialTransactions.EVENT_COST, eventCost)
                put(FinancialTransactions.USER_ID, userId)
                put(FinancialTransactions.ADDED_TIME, addedTime)
                put(FinancialTransactions.CARD_ID, cardId)
            }
        }

        val isInserted = db?.insert(FinancialTransactions.TABLE_NAME, null, contentValues)

        return isInserted != -1L
    }

    fun getDataCursor(cardId: String): Cursor {
        val db = this.readableDatabase
        val query =
            "SELECT * FROM ${FinancialTransactions.TABLE_NAME} WHERE ${FinancialTransactions.CARD_ID} = ? ORDER BY ${FinancialTransactions.ADDED_TIME} ASC"
        val cursor = db.rawQuery(query, arrayOf(cardId))

//        db.close()

        return cursor
    }

    fun deleteRow(id: Int): Boolean {
        val db = this.writableDatabase
        val isDeleted = db.delete(
            FinancialTransactions.TABLE_NAME, "${FinancialTransactions.ID} = ?",
            arrayOf(id.toString())
        )

        return isDeleted != -1
    }

    fun deleteRowsWithCardId(cardId: Int) {
        val db = this.writableDatabase
        val isDeletedRows = db.delete(
            FinancialTransactions.TABLE_NAME, "${FinancialTransactions.CARD_ID} = ?",
            arrayOf(cardId.toString())
        )
//        db.close()
    }

    fun deleteAllDataWithUserId(userId: Int): Boolean {
        val db = this.writableDatabase
        val isDeleted = db.delete(
            FinancialTransactions.TABLE_NAME, "${FinancialTransactions.USER_ID} = ?",
            arrayOf(userId.toString())
        )

        return isDeleted != -1
    }

    fun getFilteredDate(startDate: String, endDate: String): Cursor {
        val db = readableDatabase
        val query = "SELECT * FROM ${FinancialTransactions.TABLE_NAME} " +
                "WHERE ${FinancialTransactions.ADDED_TIME} BETWEEN ? AND ?"
        val cursor = db.rawQuery(query, arrayOf(startDate, endDate))
//        db.close()
        return cursor
    }

    fun getCardsCursor(userId: Int): Cursor {
        val db = this.readableDatabase
        val query =
            "SELECT * FROM ${FinancialTransactions.TABLE_NAME} WHERE ${FinancialTransactions.USER_ID} = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
//        db.close()
        return cursor
    }

}