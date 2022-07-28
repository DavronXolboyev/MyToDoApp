package com.xdmobile.to_doapp.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.xdmobile.to_doapp.constants.CardBackground
import com.xdmobile.to_doapp.database.DbConstants.Cards
import com.xdmobile.to_doapp.database.DbConstants.User
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
                put(Cards.CARD_STYLE_ID,cardStyle.id)
            }
        }
        val isInserted = db.insert(Cards.TABLE_NAME, null, contentValues)

//        db.close()
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
                put(Cards.CARD_STYLE_ID,cardStyle.id)
            }
        }
        val isUpdated = db.update(
            Cards.TABLE_NAME,
            contentValues,
            "${Cards.ID} = ?",
            arrayOf(cardModel.id.toString())
        )
//        db.close()
        return isUpdated != -1
    }

    fun deleteCard(id: Int): Boolean {
        val db = this.writableDatabase
        val isDeleted = db.delete(Cards.TABLE_NAME, "${Cards.ID} = ?", arrayOf(id.toString()))
//        db.close()
        return isDeleted != -1
    }

    fun getAllCardsCursor(userId: Int): Cursor {
        val db = this.readableDatabase
        val query = "SELECT * FROM ${Cards.TABLE_NAME} WHERE ${Cards.USER_ID} = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
//        db.close()
        return cursor
    }

    fun getCard(cardNumber: String): CardModel? {
        val db = this.readableDatabase
        val query = "SELECT * FROM ${Cards.TABLE_NAME} WHERE ${Cards.CARD_NAME} = ?"
        val cursor = db.rawQuery(query, arrayOf(cardNumber))
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
                cardStyle = CardBackground().getCardStyleById(cursor.getInt(7))
            )
        }
//        db.close()
        return null
    }

}