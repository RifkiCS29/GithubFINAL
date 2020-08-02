package com.rifki.kotlin.mygithubfinal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rifki.kotlin.mygithubfinal.R
import com.rifki.kotlin.mygithubfinal.model.GithubUser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_follow_github_user.view.*

class FollowersAdapter : RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder>() {
    private val listFollowersGithubUser = ArrayList<GithubUser>()

    fun setData(items: ArrayList<GithubUser>) {
        listFollowersGithubUser.clear()
        listFollowersGithubUser.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup : ViewGroup, viewType: Int): FollowersViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_follow_github_user, viewGroup, false)
        return FollowersViewHolder(view)
    }

    override fun getItemCount(): Int = listFollowersGithubUser.size

    override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) {
        holder.bind(listFollowersGithubUser[position])
    }

    class FollowersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(githubUser: GithubUser){
            with(itemView){
                Picasso.get()
                    .load(githubUser.avatar)
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .error(R.drawable.ic_baseline_account_circle_24)
                    .into(image_avatar)
                tv_username.text = githubUser.username
                tv_link.text = githubUser.url
            }
        }
    }
}