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

class FollowingViewModel : ViewModel() {
    val followingGithubUser = MutableLiveData<ArrayList<GithubUser>>()
    val errorMessage = MutableLiveData<Event<String>>()

    fun setFollowingGithubUser(username: String) {
        val listFollowing = ArrayList<GithubUser>()
        AndroidNetworking.get("https://api.github.com/users/$username/following")
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
                            listFollowing.add(githubUser)
                        }
                        followingGithubUser.postValue(listFollowing)
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

    fun getFollowingGithubUsers(): LiveData<ArrayList<GithubUser>> {
        return followingGithubUser
    }
}