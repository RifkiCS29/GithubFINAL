package com.rifki.kotlin.mygithubfinal.helper

import android.database.Cursor
import com.rifki.kotlin.mygithubfinal.db.DatabaseContract.FavouriteColumns.Companion.AVATAR
import com.rifki.kotlin.mygithubfinal.db.DatabaseContract.FavouriteColumns.Companion.DATE
import com.rifki.kotlin.mygithubfinal.db.DatabaseContract.FavouriteColumns.Companion.URL
import com.rifki.kotlin.mygithubfinal.db.DatabaseContract.FavouriteColumns.Companion.USERNAME
import com.rifki.kotlin.mygithubfinal.db.DatabaseContract.FavouriteColumns.Companion._ID
import com.rifki.kotlin.mygithubfinal.db.DatabaseContract.getColumnInt
import com.rifki.kotlin.mygithubfinal.db.DatabaseContract.getColumnString

class FavouritesItem (cursor: Cursor?) {
    var id : Int? = 0
    var username: String? = null
    var avatar: String? = null
    var url: String? = null
    var date: String? = null

    init {
        id = getColumnInt(cursor, _ID)
        username = getColumnString(cursor, USERNAME)
        avatar = getColumnString(cursor, AVATAR)
        url = getColumnString(cursor, URL)
        date = getColumnString(cursor, DATE)
    }

    override fun toString(): String {
        return """FavouritesItem{id = '$id',username = '$username',avatar = '$avatar',url = '$url'}"""
    }
}