package com.rifki.kotlin.mygithubfinal.widget

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.os.Binder
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.rifki.kotlin.mygithubfinal.R
import com.rifki.kotlin.mygithubfinal.db.DatabaseContract.FavouriteColumns.Companion.CONTENT_URI
import com.rifki.kotlin.mygithubfinal.helper.FavouritesItem
import java.util.concurrent.ExecutionException

class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory{

    private var listFavouriteUsers : Cursor? = null

    override fun onDataSetChanged() {
        if (listFavouriteUsers != null) {
            listFavouriteUsers?.close()
        }

        val identityToken = Binder.clearCallingIdentity()
        listFavouriteUsers = mContext.contentResolver.query(CONTENT_URI, null, null, null, null)
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun onCreate() {
        listFavouriteUsers = mContext.contentResolver.query(CONTENT_URI, null, null, null, null)
    }

    override fun getCount(): Int {
        return listFavouriteUsers?.count ?: 0
    }

    override fun getViewAt(position: Int): RemoteViews {
        val item: FavouritesItem = getItem(position)
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)

        var bitmap: Bitmap? = null
        try {
            bitmap = Glide.with(mContext)
                .asBitmap()
                .load(item.avatar)
                .submit()
                .get()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }

        rv.setImageViewBitmap(R.id.imageView, bitmap)
        val extras = Bundle()

        //ambil username
        extras.putString(FavoriteUserWidget.EXTRA_ITEM, item.username)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    private fun getItem(position: Int): FavouritesItem {
        listFavouriteUsers?.moveToPosition(position)?.let { check(it) { "Invalid Position!" } }
        return FavouritesItem(listFavouriteUsers)
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    override fun onDestroy() { }

}

