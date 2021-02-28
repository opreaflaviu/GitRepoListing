package com.example.gitreposlistingapp.ui.fragments.listfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.example.gitreposlistingapp.data.model.GitRepo
import com.example.gitreposlistingapp.databinding.RepoListItemBinding

class GitRepoAdapter(private val listener: OnRepoClickListener): PagingDataAdapter<GitRepo, GitRepoAdapter.GitRepoViewHolder>(GitRepoComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitRepoViewHolder {
        val binding = RepoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GitRepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GitRepoViewHolder, position: Int) {
        val gitRepo = getItem(position)
        gitRepo?.let {
            holder.bind(it)
        }
    }

    inner class GitRepoViewHolder(private val binding: RepoListItemBinding): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val repo = getItem(position)
                    repo?.let {
                        listener.onRepoClick(it.fullName, it.name)
                    }
                }
            }
        }

        fun bind(gitRepo: GitRepo) {
            binding.apply {
                listItemRepoName.text = gitRepo.name
                listItemRepoStars.text = gitRepo.stargazersCount.toString()
            }
        }
    }

    private object GitRepoComparator: ItemCallback<GitRepo>() {
        override fun areItemsTheSame(oldItem: GitRepo, newItem: GitRepo): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: GitRepo, newItem: GitRepo): Boolean =
            oldItem == newItem
    }

    interface OnRepoClickListener {
        fun onRepoClick(fullName: String, name: String)
    }
}