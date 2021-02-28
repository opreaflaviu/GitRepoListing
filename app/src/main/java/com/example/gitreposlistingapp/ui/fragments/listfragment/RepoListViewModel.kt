package com.example.gitreposlistingapp.ui.fragments.listfragment

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.gitreposlistingapp.data.repository.GitRepoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(private val gitRepoRepository: GitRepoRepository) : ViewModel() {

    val gitRepoList = gitRepoRepository.getGitRepoList().cachedIn(viewModelScope)
}