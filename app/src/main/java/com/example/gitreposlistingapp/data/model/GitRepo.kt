package com.example.gitreposlistingapp.data.model

import com.google.gson.annotations.SerializedName

data class GitRepo(
    val id: Int,
    val name: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("stargazers_count") val stargazersCount: Int
)