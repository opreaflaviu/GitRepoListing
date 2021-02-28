package com.example.gitreposlistingapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.gitreposlistingapp.data.GitPagingSource
import com.example.gitreposlistingapp.data.api.GitApi
import com.example.gitreposlistingapp.data.model.GitRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitRepoRepository @Inject constructor(private val gitApi: GitApi) {

    fun getGitRepoList(): LiveData<PagingData<GitRepo>> {
        val pagingConfig = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        )

        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { GitPagingSource(gitApi) }
        ).liveData
    }


}