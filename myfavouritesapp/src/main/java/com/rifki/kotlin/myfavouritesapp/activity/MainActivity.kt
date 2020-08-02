package com.rifki.kotlin.myfavouritesapp.activity

import android.database.ContentObserver
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rifki.kotlin.myfavouritesapp.R
import com.rifki.kotlin.myfavouritesapp.adapter.FavouriteAdapter
import com.rifki.kotlin.myfavouritesapp.db.DatabaseContract.FavouriteColumns.Companion.CONTENT_URI
import com.rifki.kotlin.myfavouritesapp.helper.MappingHelper
import com.rifki.kotlin.myfavouritesapp.model.GithubUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: FavouriteAdapter
    private lateinit var uriWithId: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(R.drawable.ic_github_logo)
        supportActionBar?.setDisplayUseLogoEnabled(true)

        adapter = FavouriteAdapter()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler){
            override fun onChange(self: Boolean) {
                loadFavouritesAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)
    }

    private fun showRecyclerListFavourite() {
        rv_fav_users.layoutManager = LinearLayoutManager(this)
        rv_fav_users.setHasFixedSize(true)
        rv_fav_users.adapter = adapter
        adapter.setOnItemClickCallback(object : FavouriteAdapter.OnItemClickCallback{
            override fun onItemClicked(data : GithubUser) {
                uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + data.id)
                showAlertDialog(data.username)
            }
        })
    }

    //Background Thread
    private fun loadFavouritesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
            val deferredFavourites = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI,null,null,null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressBar.visibility = View.INVISIBLE
            val favourites = deferredFavourites.await()
            if (favourites.size > 0) {
                adapter.listFavourites = favourites
            } else {
                adapter.listFavourites = ArrayList()
                val noFav = getString(R.string.no_fav)
                showSnackMessage(noFav)
            }
        }
    }

    private fun showAlertDialog(username : String) {
        val dialogMessage = getString(R.string.dialog_message)
        val dt = getString(R.string.dialog_title)
        val dialogTitle = "$dt $username"
        val yes = getString(R.string.yes)
        val no = getString(R.string.no)

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton(yes) { _, _ ->
                contentResolver.delete(uriWithId, null, null)
                val removeFav = getString(R.string.remove_user)
                showSnackMessage(removeFav)
                onResume()
            }
            .setNegativeButton(no) { dialog, _ -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showSnackMessage(message: String) {
        Snackbar.make(rv_fav_users, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        showRecyclerListFavourite()
        loadFavouritesAsync()
    }
}