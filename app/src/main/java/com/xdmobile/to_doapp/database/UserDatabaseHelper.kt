package com.xdmobile.to_doapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.xdmobile.to_doapp.database.DbConstants.User
import com.xdmobile.to_doapp.model.UserModel

class UserDatabaseHelper(val context: Context) : SQLiteOpenHelper(
    context,
    DbConstants.DATABASE_NAME, null, DbConstants.VERSION_CODE
) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("PRAGMA foreign_keys = ON")
        db?.execSQL(DbConstants.CREATE_USER_TABLE)
        db?.execSQL(DbConstants.CREATE_CARDS_TABLE)
        db?.execSQL(DbConstants.CREATE_FINANCIAL_TRANSACTIONS_TABLE)
        db?.execSQL(DbConstants.CREATE_TO_DO_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTable = "DROP TABLE IF EXISTS ${User.TABLE_NAME}"
        db?.execSQL(dropTable)
        onCreate(db)
    }

    fun insertUserData(user: UserModel): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            with(user) {
                put(User.USERNAME, username)
                put(User.EMAIL, email)
                put(User.PASSWORD, password)
            }
        }

        val isInserted = db.insert(User.TABLE_NAME, null, values)

        return isInserted != -1L
    }

    fun updateUserData(user: UserModel): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            with(user) {
                put(User.USERNAME, username)
                put(User.EMAIL, email)
                put(User.PASSWORD, password)
            }
        }
        val isUpdated =
            db.update(User.TABLE_NAME, values, "${User.ID} = ?", arrayOf(user.id.toString()))

        return isUpdated != -1
    }

    fun isHaveUsername(username: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT ${User.USERNAME} FROM ${User.TABLE_NAME} WHERE ${User.USERNAME} = ?"

        val cursor = db.rawQuery(query, arrayOf(username))

        return cursor.count > 0
    }

    fun isHaveEmail(email: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT ${User.EMAIL} FROM ${User.TABLE_NAME} WHERE ${User.EMAIL} = ?"

        val cursor = db.rawQuery(query, arrayOf(email))

        return cursor.count > 0
    }

    fun isUserRegistered(emailOrUsername: String, password: String): Boolean {
        val db = this.readableDatabase
        val query =
            "SELECT * FROM ${User.TABLE_NAME} WHERE (${User.EMAIL} = ? OR ${User.USERNAME} = ?) AND ${User.PASSWORD} = ?"
        val isRegistered = db.rawQuery(query, arrayOf(emailOrUsername, emailOrUsername, password))

        return isRegistered.count > 0
    }

    fun getUserID(emailOrUsername: String): Int? {
        val query =
            "SELECT ${User.ID} FROM ${User.TABLE_NAME} WHERE ${User.EMAIL} = ? OR ${User.USERNAME} = ? LIMIT 1"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, arrayOf(emailOrUsername, emailOrUsername))

        var id: Int? = null

        if (cursor.moveToNext()) {
            id = cursor?.getInt(0)
        }

        return id
    }

    fun getUser(id: Int): UserModel? {
        val db = this.readableDatabase
        val query = "SELECT * FROM ${User.TABLE_NAME} WHERE ${User.ID} = ?"
        val cursor = db.rawQuery(query, arrayOf(id.toString()))
        if (cursor.count > 0) {
            cursor.moveToNext()
            val username = cursor.getString(1)
            val email = cursor.getString(2)
            val password = cursor.getString(3)
            return UserModel(id, username, email, password)
        }
        return null
    }

}