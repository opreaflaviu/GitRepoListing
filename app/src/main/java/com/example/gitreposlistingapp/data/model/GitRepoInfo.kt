package com.example.gitreposlistingapp.data.model

import com.google.gson.annotations.SerializedName

data class GitRepoInfo(
    val id: Int,
    val name: String,
    @SerializedName("full_name") val fullName: String,
    val owner: GitRepoOwner,
    @SerializedName("html_url") val url: String,
    @SerializedName("forks_count") val forksCount: Int,
    val watchers: Int,

    )