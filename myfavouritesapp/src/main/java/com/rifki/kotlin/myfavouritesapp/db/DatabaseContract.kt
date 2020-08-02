package com.rifki.kotlin.myfavouritesapp.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY = "com.rifki.kotlin.mygithubfinal"
    const val SCHEME = "content"

    internal class FavouriteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favourite"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val AVATAR = "avatar"
            const val URL = "url"
            const val DATE = "date"

            val CONTENT_URI : Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}