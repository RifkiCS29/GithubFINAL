package com.rifki.kotlin.mygithubfinal.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GithubUser(
    var id : Int = 0,
    var username: String = "",
    var avatar: String? = "",
    var url: String? = "",
    var name: String? = "",
    var bio: String? = "",
    var company: String? = "",
    var location: String? = "",
    var repository: Int = 0,
    var followers: Int = 0,
    var following: Int = 0,
    var date : String? = null
) : Parcelable