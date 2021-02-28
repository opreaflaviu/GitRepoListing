package com.example.gitreposlistingapp.data.model

import com.google.gson.annotations.SerializedName

data class GitRepoOwner(
    val id: Int,
    @SerializedName("login") val username: String,
    @SerializedName("avatar_url") val avatar: String,
    @SerializedName("html_url") val gitProfile: String
)