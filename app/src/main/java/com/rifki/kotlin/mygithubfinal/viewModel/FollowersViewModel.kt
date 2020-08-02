package com.rifki.kotlin.mygithubfinal.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.rifki.kotlin.mygithubfinal.BuildConfig
import com.rifki.kotlin.mygithubfinal.helper.Event
import com.rifki.kotlin.mygithubfinal.model.GithubUser
import org.json.JSONArray

class FollowersViewModel : ViewModel() {
    val followersGithubUser = MutableLiveData<ArrayList<GithubUser>>()
    val errorMessage = MutableLiveData<Event<String>>()

    fun setFollowersGithubUser(username: String) {
        val listFollowers = ArrayList<GithubUser>()

        AndroidNetworking.get("https://api.github.com/users/$username/followers")
            .addHeaders(BuildConfig.GITHUB_API_KEY)
            .addHeaders("User-Agent", "request")
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    try {
                        for(i in 0 until response.length()){
                            val jsonObject = response.getJSONObject(i)
                            val githubUser = GithubUser()
                            githubUser.username = jsonObject.getString("login")
                            githubUser.avatar = jsonObject.getString("avatar_url")
                            githubUser.url = jsonObject.getString("html_url")
                            listFollowers.add(githubUser)
                        }
                        followersGithubUser.postValue(listFollowers)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onError(anError: ANError) {
                    Log.d("onFailure", anError.toString())

                    errorMessage.value = Event("Check Your Connectivity")
                }
            })
    }

    fun getFollowersGithubUsers(): LiveData<ArrayList<GithubUser>> {
        return followersGithubUser
    }
}