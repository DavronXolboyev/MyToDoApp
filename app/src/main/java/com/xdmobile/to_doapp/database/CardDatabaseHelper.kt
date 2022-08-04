package com.xdmobile.to_doapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.xdmobile.to_doapp.constants.CardBackground
import com.xdmobile.to_doapp.database.DbConstants.Cards
import com.xdmobile.to_doapp.model.CardModel

class CardDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DbConstants.DATABASE_NAME, null, DbConstants.VERSION_CODE) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DbConstants.CREATE_CARDS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTable = "DROP TABLE IF EXISTS ${Cards.TABLE_NAME}"
        db?.execSQL(dropTable)
        onCreate(db)
    }

    fun insertData(cardModel: CardModel): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            with(cardModel) {
                put(Cards.CARD_NUMBER, cardNumber)
                put(Cards.CARD_DATE, cardDate)
                put(Cards.CARD_NAME, cardName)
                put(Cards.CARD_BALANCE, cardBalance)
                put(Cards.CARD_TYPE, cardType)
                put(Cards.USER_ID, userId)
                put(Cards.CARD_STYLE_ID, cardStyle.id)
                put(Cards.CARD_EXPENSES, cardExpenses)
                put(Cards.CARD_RECEIPTS, cardReceipts)
            }
        }
        val isInserted = db.insert(Cards.TABLE_NAME, null, contentValues)

        return isInserted != -1L
    }

    fun updateData(cardModel: CardModel): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            with(cardModel) {
                put(Cards.CARD_NUMBER, cardNumber)
                put(Cards.CARD_DATE, cardDate)
                put(Cards.CARD_NAME, cardName)
                put(Cards.CARD_BALANCE, cardBalance)
                put(Cards.CARD_TYPE, cardType)
                put(Cards.USER_ID, userId)
                put(Cards.CARD_STYLE_ID, cardStyle.id)
                put(Cards.CARD_EXPENSES, cardExpenses)
                put(Cards.CARD_RECEIPTS, cardReceipts)
            }
        }
        val isUpdated = db.update(
            Cards.TABLE_NAME,
            contentValues,
            "${Cards.ID} = ?",
            arrayOf(cardModel.id.toString())
        )
        return isUpdated != -1
    }

    fun deleteCard(id: Int): Boolean {
        val db = this.writableDatabase
        val isDeleted = db.delete(Cards.TABLE_NAME, "${Cards.ID} = ?", arrayOf(id.toString()))
        return isDeleted != -1
    }

    fun deleteAllCardsWithUserId(userId: Int): Boolean {
        val db = this.writableDatabase
        val isDeleted =
            db.delete(Cards.TABLE_NAME, "${Cards.USER_ID} = ?", arrayOf(userId.toString()))

        return isDeleted != -1
    }

    fun getCardBalance(cardId: Int): Float {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT ${Cards.CARD_BALANCE} FROM ${Cards.TABLE_NAME} WHERE ${Cards.ID} = ?",
            arrayOf(cardId.toString())
        )
        cursor.moveToNext()

        return cursor.getFloat(0)
    }

    fun getAllCardsCursor(userId: Int): Cursor {
        val db = this.readableDatabase
        val query = "SELECT * FROM ${Cards.TABLE_NAME} WHERE ${Cards.USER_ID} = ?"
        return db.rawQuery(query, arrayOf(userId.toString()))
    }

    fun getCard(cardId: Int): CardModel? {
        val db = this.readableDatabase
        val query = "SELECT * FROM ${Cards.TABLE_NAME} WHERE ${Cards.ID} = ?"
        val cursor = db.rawQuery(query, arrayOf(cardId.toString()))
        if (cursor.moveToNext()) {
            db.close()
            return CardModel(
                id = cursor.getInt(0),
                cardNumber = cursor.getString(1),
                cardDate = cursor.getString(2),
                cardName = cursor.getString(3),
                cardBalance = cursor.getFloat(4),
                cardType = cursor.getString(5),
                userId = cursor.getInt(6),
                cardStyle = CardBackground().getCardStyleById(cursor.getInt(7)),
                cardExpenses = cursor.getFloat(8),
                cardReceipts = cursor.getFloat(9)
            )
        }
        return null
    }

    private fun getOldCost(cardId: Int): Float {
        val db = this.readableDatabase
        val query = "SELECT ${Cards.CARD_EXPENSES} FROM ${Cards.TABLE_NAME} WHERE ${Cards.ID} = ?"
        val cursor = db.rawQuery(query, arrayOf(cardId.toString()))
        cursor.moveToNext()
        return cursor.getFloat(0)
    }

    fun updateExpense(cardId: Int, cost: Float): Boolean {
        val db = this.writableDatabase
//        val oldCost = getOldCost(cardId)
        val contentValues = ContentValues().apply {
            put(Cards.CARD_EXPENSES, cost)
        }
        val isTheCostAdded = db.update(
            Cards.TABLE_NAME,
            contentValues,
            "${Cards.ID} = ?",
            arrayOf(cardId.toString())
        )

        return isTheCostAdded != -1
    }

    fun updateReceipt(cardId: Int, cost: Float): Boolean {
        val db = this.writableDatabase
//        val oldCost = getOldCost(cardId)
        val contentValues = ContentValues().apply {
            put(Cards.CARD_RECEIPTS, cost)
        }
        val isTheCostAdded = db.update(
            Cards.TABLE_NAME,
            contentValues,
            "${Cards.ID} = ?",
            arrayOf(cardId.toString())
        )

        return isTheCostAdded != -1
    }

    fun updateCardStyle(cardId: Int, cardStyleId: Int): Boolean {
        val db = this.writableDatabase

        val contentValues = ContentValues().apply {
            put(Cards.CARD_STYLE_ID, cardStyleId)
        }
        val isUpdated = db.update(
            Cards.TABLE_NAME,
            contentValues,
            "${Cards.ID} = ?",
            arrayOf(cardId.toString())
        )
        return isUpdated != -1
    }

}