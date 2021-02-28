package com.example.gitreposlistingapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gitreposlistingapp.data.api.GitApi
import com.example.gitreposlistingapp.data.model.GitRepo
import retrofit2.HttpException
import java.io.IOException

const val STARTING_PAGE_INDEX = 1
const val DEFAULT_SEARCH_QUERY = "android"
const val DEFAULT_SORT_BY = "stars"
const val DEFAULT_ORDER_BY = "desc"

class GitPagingSource(private val gitApi: GitApi): PagingSource<Int, GitRepo>() {

    private val TAG = GitPagingSource::class.java.canonicalName

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GitRepo> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = gitApi.getRepositoryList(
                DEFAULT_SEARCH_QUERY,
                DEFAULT_SORT_BY,
                DEFAULT_ORDER_BY,
                params.loadSize,
                position
            )
            val gitRepoList = response.items

            LoadResult.Page(
                data = gitRepoList,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (gitRepoList.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            Log.d(TAG, "IOException $exception")
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            Log.d(TAG, "HttpException $exception")
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GitRepo>): Int? {
        return state.anchorPosition
    }
}