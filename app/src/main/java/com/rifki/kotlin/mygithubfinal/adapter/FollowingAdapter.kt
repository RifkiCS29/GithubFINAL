package com.rifki.kotlin.mygithubfinal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rifki.kotlin.mygithubfinal.R
import com.rifki.kotlin.mygithubfinal.model.GithubUser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_follow_github_user.view.*

class FollowingAdapter : RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {
    private val listFollowingGithubUser = ArrayList<GithubUser>()

    fun setData(items: ArrayList<GithubUser>) {
        listFollowingGithubUser.clear()
        listFollowingGithubUser.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup : ViewGroup, viewType: Int): FollowingViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_follow_github_user, viewGroup, false)
        return FollowingViewHolder(view)
    }

    override fun getItemCount(): Int = listFollowingGithubUser.size

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        holder.bind(listFollowingGithubUser[position])
    }

    class FollowingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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