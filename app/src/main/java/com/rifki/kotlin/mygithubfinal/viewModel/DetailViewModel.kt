package com.rifki.kotlin.mygithubfinal.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.rifki.kotlin.mygithubfinal.BuildConfig
import com.rifki.kotlin.mygithubfinal.model.GithubUser
import org.json.JSONObject

class DetailViewModel : ViewModel() {
    private val detailGithubUser = MutableLiveData<GithubUser>()

    fun setDetailGithubUser(username: String) {
        AndroidNetworking.get("https://api.github.com/users/$username")
            .addHeaders(BuildConfig.GITHUB_API_KEY)
            .addHeaders("User-Agent", "request")
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        val githubUser = GithubUser()
                        githubUser.id = response.getInt("id")
                        githubUser.avatar = response.getString("avatar_url")
                        githubUser.name = response.getString("name")
                        githubUser.username = response.getString("login")
                        githubUser.repository = response.getInt("public_repos")
                        githubUser.bio = response.getString("bio")
                        githubUser.company = response.getString("company").toString()
                        githubUser.location = response.getString("location")
                        detailGithubUser.postValue(githubUser)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onError(anError: ANError) {
                    Log.d("onFailure", anError.toString())
                }
            })
    }

    fun getDetailGithubUsers(): LiveData<GithubUser> {
        return detailGithubUser
    }
}