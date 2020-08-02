package com.rifki.kotlin.mygithubfinal.adapter

import android.content.Context
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rifki.kotlin.mygithubfinal.R
import com.rifki.kotlin.mygithubfinal.view.fragment.FollowersFragment
import com.rifki.kotlin.mygithubfinal.view.fragment.FollowingFragment

class SectionsPagerAdapter(private val context: Context, fm : FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var username : String? = "username"

    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.tab_followers, R.string.tab_following)

    fun setData(usernameGithub : String){
        username = usernameGithub
    }

    private fun getData(): String? {
        return username
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = FollowersFragment()
                val bundle = Bundle()
                bundle.putString(FollowersFragment.EXTRA_FOLLOWERS, getData())
                fragment.arguments = bundle
            }
            1 -> {
                fragment = FollowingFragment()
                val bundle = Bundle()
                bundle.putString(FollowingFragment.EXTRA_FOLLOWING, getData())
                fragment.arguments = bundle
            }
        }
        return fragment as Fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }
}