package com.example.gitreposlistingapp.ui.fragments.detailsfragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.gitreposlistingapp.R
import com.example.gitreposlistingapp.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment: Fragment(R.layout.fragment_details) {

    private val detailsViewModel: DetailsViewModel by activityViewModels()

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDetailsBinding.bind(view)

        getDetails(args.fullName)
        bindData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindData() {
        listenForPageStatus()
        listenForPageData()
    }

    private fun getDetails(fullName: String) {
        val owner = fullName.split("/")[0]
        val repoName = fullName.split("/")[1]
        detailsViewModel.getRepoDetails(owner, repoName)
    }

    private fun listenForPageStatus() {
        detailsViewModel.statusLiveData.observe(viewLifecycleOwner) { detailsPageStatus ->
            detailsPageStatus?.let { pageStatus ->
                when(pageStatus) {
                    DetailsPageStatus.LOADING ->{
                        binding.apply {
                            detailPageProgressBar.isVisible = true
                            detailPageNoResultsText.isVisible = false
                            detailPageScrollView.isVisible = false
                        }
                    }
                    DetailsPageStatus.LOADED -> {
                        binding.apply {
                            detailPageProgressBar.isVisible = false
                            detailPageNoResultsText.isVisible = false
                            detailPageScrollView.isVisible = true
                        }
                    }
                    DetailsPageStatus.NO_DETAILS -> {
                        binding.apply {
                            detailPageProgressBar.isVisible = false
                            detailPageNoResultsText.isVisible = true
                            detailPageScrollView.isVisible = false
                        }
                    }
                }
            }
        }
    }

    private fun listenForPageData() {
        detailsViewModel.repoDetailsLiveData.observe(viewLifecycleOwner) { gitRepoDetails ->
            gitRepoDetails.gitRepoInfo?.let { gitRepoInfo ->
                loadImage(gitRepoInfo.owner.avatar)

                binding.apply {
                    detailUserName.text = gitRepoInfo.owner.username

                    detailUserProfileURL.apply {
                        text = gitRepoInfo.owner.gitProfile
                        paint.isUnderlineText = true
                        setOnClickListener {
                            openUrl(gitRepoInfo.owner.gitProfile)
                        }
                    }

                    textViewRepoName.text = gitRepoInfo.name

                    textViewRepoURL.apply {
                        text = gitRepoInfo.url
                        paint.isUnderlineText = true
                        setOnClickListener {
                            openUrl(gitRepoInfo.url)
                        }
                    }

                    val watchersText = "${getString(R.string.watchers)} ${gitRepoInfo.watchers}"
                    textViewWatchers.text = watchersText

                    val forkCountText = "${getString(R.string.fork_count)} ${gitRepoInfo.forksCount}"
                    textViewForksCount.text = forkCountText
                }
            }

            gitRepoDetails.readme?.let { readme ->
                binding.textViewReadmeContent.text = readme
            }
        }
    }

    private fun loadImage(imageUrl: String) {
        Glide.with(this)
            .load(imageUrl)
            .error(R.drawable.ic_error)
            .into(binding.detailImageView)
    }

    private fun openUrl(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}