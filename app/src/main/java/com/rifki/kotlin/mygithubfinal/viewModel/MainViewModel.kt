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
import com.rifki.kotlin.mygithubfinal.helper.Event
import com.rifki.kotlin.mygithubfinal.model.GithubUser
import org.json.JSONObject

class MainViewModel : ViewModel() {
    private val listGithubUsers = MutableLiveData<ArrayList<GithubUser>>()
    val errorMessage = MutableLiveData<Event<String>>()

    fun setSearchGithubUsers(username: String) {
        val listUsers = ArrayList<GithubUser>()
        AndroidNetworking.get("https://api.github.com/search/users?q=$username")
            .addHeaders(BuildConfig.GITHUB_API_KEY)
            .addHeaders("User-Agent", "request")
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        val jsonArray = response.getJSONArray("items")
                        if (jsonArray.length() == 0){
                            errorMessage.value = Event("Data Not Found")
                        }
                        for (i in 0 until jsonArray.length()) {
                            val user = jsonArray.getJSONObject(i)
                            val githubUser = GithubUser()
                            githubUser.id = user.getInt("id")
                            githubUser.username = user.getString("login")
                            githubUser.avatar = user.getString("avatar_url")
                            githubUser.url = user.getString("html_url")
                            listUsers.add(githubUser)
                        }
                        listGithubUsers.postValue(listUsers)
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

    fun getSearchGithubUsers(): LiveData<ArrayList<GithubUser>> {
        return listGithubUsers
    }
}