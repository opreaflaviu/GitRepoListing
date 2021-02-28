package com.example.gitreposlistingapp.data.api

import com.example.gitreposlistingapp.data.model.GitRepoInfo
import com.example.gitreposlistingapp.data.model.GitRepoList
import com.example.gitreposlistingapp.data.model.GitRepoReadmeContent
import retrofit2.http.*

interface GitApi {

    @Headers("Accept:application/vnd.github.v3+json")
    @GET("/search/repositories")
    suspend fun getRepositoryList(
        @Query("q") query: String,
        @Query("sort") sortBy: String,
        @Query("order") orderBy: String,
        @Query("per_page") perPage: Int,
        @Query("page") pageNumber: Int
    ): GitRepoList

    @GET("/repos/{owner}/{repo}")
    suspend fun getRepositoryInfo(
        @Path("owner") owner: String,
        @Path("repo") repoName: String
    ): GitRepoInfo

    @GET("/repos/{owner}/{repo}/contents/README.md")
    suspend fun getRepositoryReadmeContent(
        @Path("owner") owner: String,
        @Path("repo") repoName: String
    ): GitRepoReadmeContent

    companion object {
        const val BASE_URL = "https://api.github.com/"
    }
}