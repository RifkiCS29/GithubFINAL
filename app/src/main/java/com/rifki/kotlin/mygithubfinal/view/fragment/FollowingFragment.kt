package com.rifki.kotlin.mygithubfinal.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rifki.kotlin.mygithubfinal.R
import com.rifki.kotlin.mygithubfinal.adapter.FollowingAdapter
import com.rifki.kotlin.mygithubfinal.viewModel.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_following.*

/**
 * A simple [Fragment] subclass.
 */
class FollowingFragment : Fragment() {
    private lateinit var adapter: FollowingAdapter
    private lateinit var followingViewModel: FollowingViewModel

    companion object {
        const val EXTRA_FOLLOWING = "extra_following"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowingAdapter()
        showRecyclerView()

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)
        if(arguments != null){
            val username = arguments?.getString(EXTRA_FOLLOWING)
            showLoading(true)
            followingViewModel.setFollowingGithubUser(username.toString())
        }

        followingViewModel.getFollowingGithubUsers().observe(viewLifecycleOwner, Observer { githubUser ->
            if(githubUser != null){
                adapter.setData(githubUser)
                showLoading(false)
            }
        })

        followingViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
                showLoading(false)
            }
        })
    }

    private fun showRecyclerView() {
        rv_following_github_user.layoutManager = LinearLayoutManager(context)
        rv_following_github_user.adapter = adapter

        adapter.notifyDataSetChanged()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

}
