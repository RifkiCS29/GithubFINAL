package com.rifki.kotlin.mygithubfinal.view.activity

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.ContentValues
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.rifki.kotlin.mygithubfinal.R
import com.rifki.kotlin.mygithubfinal.adapter.SectionsPagerAdapter
import com.rifki.kotlin.mygithubfinal.db.DatabaseContract
import com.rifki.kotlin.mygithubfinal.db.DatabaseContract.FavouriteColumns.Companion.CONTENT_URI
import com.rifki.kotlin.mygithubfinal.helper.MappingHelper
import com.rifki.kotlin.mygithubfinal.model.GithubUser
import com.rifki.kotlin.mygithubfinal.view.fragment.FollowersFragment
import com.rifki.kotlin.mygithubfinal.view.fragment.FollowingFragment
import com.rifki.kotlin.mygithubfinal.viewModel.DetailViewModel
import com.rifki.kotlin.mygithubfinal.widget.FavoriteUserWidget
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {
    private lateinit var detailViewModel: DetailViewModel
    private var isFavourite = false
    private lateinit var uriWithId : Uri
    private var menuItem: Menu? = null
    private lateinit var detailGithubUser: GithubUser

    companion object {
        const val EXTRA_STATE = "extra_state"
        internal val TAG = DetailActivity::class.java.simpleName
    }

    private fun setActionBarTitle(username: String) {
        if (supportActionBar != null) {
            (supportActionBar as ActionBar).title = username
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        detailGithubUser = intent.getParcelableExtra(EXTRA_STATE) as GithubUser

        Picasso.get()
            .load(detailGithubUser.avatar)
            .placeholder(R.drawable.ic_baseline_account_circle_24)
            .error(R.drawable.ic_baseline_account_circle_24)
            .into(image_avatar)
        tv_username.text = detailGithubUser.username

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)

        detailViewModel.setDetailGithubUser(detailGithubUser.username)
        detailViewModel.getDetailGithubUsers().observe(this, Observer { githubUser ->
            if(githubUser != null) {
                tv_name.text = githubUser.name
                tv_repo.text = githubUser.repository.toString()
                tv_company.text = githubUser.company
                tv_bio.text = githubUser.bio
                tv_location.text = githubUser.location
            }
        })

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.setData(detailGithubUser.username)
        view_pager.adapter = sectionsPagerAdapter
        tabs_follow.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f

        val bundle = Bundle()
        val followersFragment = FollowersFragment()
        bundle.putString(FollowersFragment.EXTRA_FOLLOWERS, detailGithubUser.username)
        followersFragment.arguments = bundle

        val followingFragment = FollowingFragment()
        bundle.putString(FollowingFragment.EXTRA_FOLLOWING, detailGithubUser.username)
        followingFragment.arguments = bundle

        setActionBarTitle(detailGithubUser.username)

        favouriteCheck()
    }

    private fun favouriteCheck(){
        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + detailGithubUser.id)
        val cursor = contentResolver.query(uriWithId, null, null, null, null)

        val favourite = MappingHelper.mapCursorToArrayList(cursor)
        for (data in favourite) {
            if (detailGithubUser.id == data.id) {
                isFavourite = true
                Log.d(TAG, "this User Favorite")
            }
        }
    }

    private fun insertFavouriteUser(){
        val values = ContentValues().apply {
            put(DatabaseContract.FavouriteColumns._ID, detailGithubUser.id)
            put(DatabaseContract.FavouriteColumns.USERNAME, detailGithubUser.username)
            put(DatabaseContract.FavouriteColumns.AVATAR, detailGithubUser.avatar)
            put(DatabaseContract.FavouriteColumns.URL, detailGithubUser.url)
            put(DatabaseContract.FavouriteColumns.DATE, getCurrentDate())
        }
        contentResolver.insert(CONTENT_URI, values)
        val favUser = getString(R.string.fav)
        showSnackMessage("${detailGithubUser.username} $favUser")
    }

    private fun deleteFavouriteUser(){
        contentResolver.delete(uriWithId, null, null)
        val unFav = getString(R.string.un_fav)
        showSnackMessage("${detailGithubUser.username} $unFav")
        Log.d(TAG, "Deleted : $uriWithId")
    }

    private fun setFavouriteIcon(){
        if (isFavourite){
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_24)
        } else {
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_border_24)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu_detail, menu)
        menuItem = menu
        setFavouriteIcon()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                if (isFavourite) deleteFavouriteUser() else insertFavouriteUser()

                isFavourite = !isFavourite
                setFavouriteIcon()
                reloadWidget()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    private fun showSnackMessage(message: String) {
        Snackbar.make(view_pager, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun reloadWidget() {
        val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
        val thisAppWidget = ComponentName(applicationContext.packageName, FavoriteUserWidget::class.java.name)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget)
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view)
    }
}

