package com.rifki.kotlin.mygithubfinal.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.rifki.kotlin.mygithubfinal.db.DatabaseContract.FavouriteColumns.Companion.DATE
import com.rifki.kotlin.mygithubfinal.db.DatabaseContract.FavouriteColumns.Companion.TABLE_NAME
import com.rifki.kotlin.mygithubfinal.db.DatabaseContract.FavouriteColumns.Companion._ID
import java.sql.SQLException

class FavouriteHelper(context: Context) {

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private var INSTANCE : FavouriteHelper? = null

        fun getInstance(context: Context) : FavouriteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavouriteHelper(context)
            }

        private lateinit var database : SQLiteDatabase
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }

    //membuka dan menutup database
    @Throws(SQLException::class)
    fun open() {
        database  = databaseHelper.writableDatabase
    }

    fun queryAll() : Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$DATE DESC")
    }

    //Ambil data berdasarkan _ID
    fun queryById(id : String) : Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$_ID= ?",
            arrayOf(id),
            null,
            null,
            null,
            null)
    }

    fun insert(values: ContentValues?) : Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteById(id: String) : Int {
        return database.delete(DATABASE_TABLE,"$_ID = '$id'", null)
    }
}