package com.example.gitreposlistingapp.data.repository

import com.example.gitreposlistingapp.data.api.GitApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepoDetailsRepository @Inject constructor(private val gitApi: GitApi) {

    suspend fun getRepositoryInfo(owner: String, repoName: String) = gitApi.getRepositoryInfo(owner, repoName)

    suspend fun getRepositoryReadmeContent(owner: String, repoName: String) = gitApi.getRepositoryReadmeContent(owner, repoName)
}