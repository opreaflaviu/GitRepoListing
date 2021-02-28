package com.example.gitreposlistingapp.ui.fragments.listfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gitreposlistingapp.databinding.GitRepoLoadStateItemBinding

class GitRepoLoadStateAdapter(private var retryAction: () -> Unit): LoadStateAdapter<GitRepoLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = GitRepoLoadStateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(private val binding: GitRepoLoadStateItemBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.loadStateRetryButton.setOnClickListener {
                retryAction.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                loadStateProgressBar.isVisible = loadState is LoadState.Loading
                loadStateErrorText.isVisible = loadState !is LoadState.Loading
                loadStateRetryButton.isVisible = loadState !is LoadState.Loading
            }
        }
    }
}