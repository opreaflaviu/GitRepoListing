package com.example.gitreposlistingapp.ui.fragments.detailsfragment

import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gitreposlistingapp.data.model.GitRepoDetails
import com.example.gitreposlistingapp.data.model.GitRepoInfo
import com.example.gitreposlistingapp.data.repository.RepoDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repoDetailsRepository: RepoDetailsRepository): ViewModel() {

    private val TAG = DetailsViewModel::class.java.canonicalName


    private var repoInfo: GitRepoInfo? = null
    private var readmeContent: String? = null

    private val pageStatus = MutableLiveData<DetailsPageStatus>(DetailsPageStatus.LOADING)
    val statusLiveData: LiveData<DetailsPageStatus> get() = pageStatus

    private val repoDetails = MutableLiveData<GitRepoDetails>()
    val repoDetailsLiveData: LiveData<GitRepoDetails> get() = repoDetails

    fun getRepoDetails(owner: String, repoName: String) {
        pageStatus.value = DetailsPageStatus.LOADING
        viewModelScope.launch {
            val job1 = viewModelScope.launch {
                try {
                    val gitRepoInfo = repoDetailsRepository.getRepositoryInfo(owner, repoName)
                    repoInfo = gitRepoInfo
                } catch (exception: Exception) {
                    Log.d(TAG, "getRepositoryInfo Exception - $exception")
                }
            }

            val job2 = viewModelScope.launch {
                try {
                    val gitRepoReadmeContent =
                        repoDetailsRepository.getRepositoryReadmeContent(owner, repoName)
                    val readmeContentString =
                        String(Base64.decode(gitRepoReadmeContent.content, Base64.DEFAULT))
                    readmeContent = readmeContentString
                } catch (exception: Exception) {
                    Log.d(TAG, "getRepositoryReadmeContent Exception - $exception")
                }
            }

            joinAll(job1, job2)

            if (repoInfo != null || readmeContent != null) {
                pageStatus.postValue(DetailsPageStatus.LOADED)
                val details = GitRepoDetails(repoInfo, readmeContent)
                repoInfo = null
                readmeContent = null
                repoDetails.postValue(details)
            } else {
                pageStatus.postValue(DetailsPageStatus.NO_DETAILS)
            }
        }
    }
}