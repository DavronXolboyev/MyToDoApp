package com.xdmobile.to_doapp.database


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.xdmobile.to_doapp.database.DbConstants.ToDoTable
import com.xdmobile.to_doapp.database.DbConstants.User
import com.xdmobile.to_doapp.model.ToDoModel
import com.xdmobile.to_doapp.model.UserModel

class ToDoDatabaseHelper(val context: Context) :
    SQLiteOpenHelper(context, DbConstants.DATABASE_NAME, null, DbConstants.VERSION_CODE) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            "CREATE TABLE ${ToDoTable.TABLE_NAME} (" +
                    "${ToDoTable.ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${ToDoTable.INTENDED_WORK} TEXT NOT NULL," +
                    "${ToDoTable.DATE} DATE NOT NULL," +
                    "${ToDoTable.IS_FINISHED} INTEGER NOT NULL DEFAULT 0," +
                    "${ToDoTable.USER_ID} INTEGER," +
                    "FOREIGN KEY(${ToDoTable.USER_ID}) REFERENCES ${User.TABLE_NAME}(${User.ID})" +
                    ")"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTable = "DROP TABLE IF EXISTS ${ToDoTable.TABLE_NAME}"
        db?.execSQL(dropTable)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        super.onDowngrade(db, oldVersion, newVersion)
    }

    fun putData(toDoModel: ToDoModel, userModel: UserModel): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(ToDoTable.INTENDED_WORK, toDoModel.intendedWork)
            put(ToDoTable.DATE, toDoModel.date)
            put(ToDoTable.USER_ID, userModel.id)
        }

        val isInserted = db.insert(ToDoTable.TABLE_NAME, null, contentValues)

        db.close()
        return isInserted != -1L
    }

    fun deleterRow(id: Int): Boolean {
        val db = this.writableDatabase
        val isDeleted = db.delete(ToDoTable.TABLE_NAME, "$id LIKE ?", arrayOf(id.toString()))

        db.close()
        return isDeleted != -1
    }

    // #########################
    fun getDataCursor(userModel: UserModel): Cursor {
        val db = this.readableDatabase
        val cursor =
            db.rawQuery("SELECT * FROM ${ToDoTable.TABLE_NAME} WHERE ${ToDoTable.USER_ID} = ?",
                arrayOf(userModel.id.toString()))

        db.close()
        return cursor
    }

    fun update(toDoModel: ToDoModel): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            with(toDoModel) {
                put(ToDoTable.INTENDED_WORK, intendedWork)
                put(ToDoTable.DATE, date)
                put(ToDoTable.IS_FINISHED, if (isFinished) 1 else 0)
            }
        }
        val isUpdated = db.update(
            ToDoTable.TABLE_NAME,
            contentValues,
            "${toDoModel.id} = ?",
            arrayOf(toDoModel.id.toString())
        )

        return isUpdated != -1
    }
}