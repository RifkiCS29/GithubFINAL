package com.rifki.kotlin.mygithubfinal.helper

import android.database.Cursor
import com.rifki.kotlin.mygithubfinal.db.DatabaseContract
import com.rifki.kotlin.mygithubfinal.model.GithubUser

object MappingHelper {
    fun mapCursorToArrayList(usersCursor : Cursor?) : ArrayList<GithubUser>{
        val usersList = ArrayList<GithubUser>()

        usersCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavouriteColumns._ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavouriteColumns.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavouriteColumns.AVATAR))
                val url = getString(getColumnIndexOrThrow(DatabaseContract.FavouriteColumns.URL))
                usersList.add(GithubUser(id, username, avatar, url))
            }
        }
        return usersList
    }
}