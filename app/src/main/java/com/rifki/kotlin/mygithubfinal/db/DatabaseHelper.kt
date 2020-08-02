package com.rifki.kotlin.mygithubfinal.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.rifki.kotlin.mygithubfinal.db.DatabaseContract.FavouriteColumns.Companion.TABLE_NAME
import com.rifki.kotlin.mygithubfinal.db.DatabaseContract.FavouriteColumns

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object{
        private const val DATABASE_NAME = "dbgithubfinalapp"
        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                "(${FavouriteColumns._ID} INT PRIMARY KEY," +
                " ${FavouriteColumns.USERNAME} TEXT NOT NULL UNIQUE," +
                " ${FavouriteColumns.AVATAR} TEXT NOT NULL," +
                " ${FavouriteColumns.URL} TEXT NOT NULL," +
                " ${FavouriteColumns.DATE} TEXT NOT NULL)"

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}