package com.rifki.kotlin.mygithubfinal.view.activity

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rifki.kotlin.mygithubfinal.R
import com.rifki.kotlin.mygithubfinal.adapter.FavouriteAdapter
import com.rifki.kotlin.mygithubfinal.db.DatabaseContract.FavouriteColumns.Companion.CONTENT_URI
import com.rifki.kotlin.mygithubfinal.helper.MappingHelper
import com.rifki.kotlin.mygithubfinal.model.GithubUser
import kotlinx.android.synthetic.main.activity_favourite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavouriteActivity : AppCompatActivity() {

    private lateinit var adapter: FavouriteAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        supportActionBar?.title = getString(R.string.fav)

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

        //mempertahankan data jika rotasi layar
        if (savedInstanceState == null) {
            // proses ambil data
            loadFavouritesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<GithubUser>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavourites = list
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavourites)
    }

    private fun showRecyclerListFavourite() {
        rv_fav_users.layoutManager = LinearLayoutManager(this)
        rv_fav_users.setHasFixedSize(true)
        rv_fav_users.adapter = adapter
        adapter.setOnItemClickCallback(object : FavouriteAdapter.OnItemClickCallback{
            override fun onItemClicked(data : GithubUser) {
                val intent = Intent(this@FavouriteActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_STATE, data)
                startActivity(intent)
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

    private fun showSnackMessage(message: String) {
        Snackbar.make(rv_fav_users, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        showRecyclerListFavourite()
        loadFavouritesAsync()
    }
}