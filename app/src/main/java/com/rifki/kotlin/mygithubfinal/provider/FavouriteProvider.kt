package com.rifki.kotlin.mygithubfinal.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.rifki.kotlin.mygithubfinal.db.DatabaseContract.AUTHORITY
import com.rifki.kotlin.mygithubfinal.db.DatabaseContract.FavouriteColumns.Companion.CONTENT_URI
import com.rifki.kotlin.mygithubfinal.db.DatabaseContract.FavouriteColumns.Companion.TABLE_NAME
import com.rifki.kotlin.mygithubfinal.db.FavouriteHelper

class FavouriteProvider : ContentProvider() {

    companion object {
        private const val FAVOURITE = 1
        private const val FAVOURITE_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favouriteHelper: FavouriteHelper

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVOURITE)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", FAVOURITE_ID)
        }
    }

    override fun onCreate(): Boolean {
        favouriteHelper = FavouriteHelper.getInstance(context as Context)
        favouriteHelper.open()
        return true
    }

    override fun query(uri: Uri, strings: Array<String>?, s: String?, strings1: Array<String>?, s1: String?): Cursor?  {
        val cursor : Cursor?
        when(sUriMatcher.match(uri)){
            FAVOURITE -> cursor = favouriteHelper.queryAll()
            FAVOURITE_ID -> cursor = favouriteHelper.queryById(uri.lastPathSegment.toString())
            else -> cursor = null
        }
        return cursor
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added : Long = when (FAVOURITE){
            sUriMatcher.match(uri) -> favouriteHelper.insert(contentValues)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int  {
        val deleted: Int = when (FAVOURITE_ID) {
            sUriMatcher.match(uri) -> favouriteHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun update(uri: Uri, contentValues: ContentValues?, s: String?, strings: Array<String>?): Int {
        return 0
    }
}
