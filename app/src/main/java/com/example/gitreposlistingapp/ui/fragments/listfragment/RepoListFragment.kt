package com.example.gitreposlistingapp.ui.fragments.listfragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.gitreposlistingapp.R
import com.example.gitreposlistingapp.databinding.FragmentRepoListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepoListFragment: Fragment(R.layout.fragment_repo_list), GitRepoAdapter.OnRepoClickListener {

    var _binding: FragmentRepoListBinding? = null
    val binding get() = _binding!!

    private lateinit var gitRepoAdapter: GitRepoAdapter

    private val repoListViewModel: RepoListViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRepoListBinding.bind(view)

        gitRepoAdapter = GitRepoAdapter(this)

        bindData()
        observeGitRepoList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindData() {
        binding.apply {
            recyclerViewRepoList.apply {
                setHasFixedSize(true)
                itemAnimator = null
                adapter = gitRepoAdapter.withLoadStateHeaderAndFooter(
                    header = GitRepoLoadStateAdapter {
                        gitRepoAdapter.retry()
                    },

                    footer = GitRepoLoadStateAdapter {
                        gitRepoAdapter.retry()
                    }
                )
            }

            retryButton.setOnClickListener {
                gitRepoAdapter.retry()
            }
        }

        gitRepoAdapter.addLoadStateListener { combinedLoadStates ->
            binding.apply {
                repoListProgressBar.isVisible = combinedLoadStates.source.refresh is LoadState.Loading
                recyclerViewRepoList.isVisible = combinedLoadStates.source.refresh is LoadState.NotLoading
                retryButton.isVisible = combinedLoadStates.source.refresh is LoadState.Error
                errorTextView.isVisible = combinedLoadStates.source.refresh is LoadState.Error

                if (combinedLoadStates.source.refresh is LoadState.NotLoading &&
                        combinedLoadStates.refresh.endOfPaginationReached &&
                        gitRepoAdapter.itemCount < 1) {
                    recyclerViewRepoList.isVisible = false
                    noResultsText.isVisible = true
                } else {
                    noResultsText.isVisible = false
                }
            }
        }
    }

    private fun observeGitRepoList() {
        repoListViewModel.gitRepoList.observe(viewLifecycleOwner) {
            gitRepoAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onRepoClick(fullName: String, name: String) {
        val action = RepoListFragmentDirections.actionRepositoryListFragmentToDetailsFragment2(fullName, name)
        findNavController().navigate(action)
    }
}