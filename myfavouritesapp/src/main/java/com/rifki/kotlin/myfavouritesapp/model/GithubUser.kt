package com.rifki.kotlin.myfavouritesapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GithubUser(
    var id : Int = 0,
    var username: String = "",
    var avatar: String? = "",
    var url: String? = ""
) : Parcelable