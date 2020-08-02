package com.rifki.kotlin.myfavouritesapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rifki.kotlin.myfavouritesapp.R
import com.rifki.kotlin.myfavouritesapp.model.GithubUser
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_row_github.view.*

class FavouriteAdapter : RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    var listFavourites = ArrayList<GithubUser>()
        set(listFavourites) {
            if (listFavourites.size >= 0){
                this.listFavourites.clear()
            }
            this.listFavourites.addAll(listFavourites)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_github, viewGroup, false)
        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int = this.listFavourites.size

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        holder.bind(listFavourites[position])
    }

    inner class FavouriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(githubUser: GithubUser){
            with(itemView){
                Picasso.get()
                    .load(githubUser.avatar)
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .error(R.drawable.ic_baseline_account_circle_24)
                    .into(image_avatar)
                tv_username.text = githubUser.username
                tv_link.text = githubUser.url

                itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(githubUser)
                }
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: GithubUser)
    }
}