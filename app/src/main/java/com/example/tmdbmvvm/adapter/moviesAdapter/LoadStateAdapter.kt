package com.example.tmdbmvvm.adapter.moviesAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbmvvm.databinding.LoadStateFooterBinding

class LoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<com.example.tmdbmvvm.adapter.moviesAdapter.LoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            LoadStateFooterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }


    inner class LoadStateViewHolder(private val binding: LoadStateFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnTry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.tvError.text = loadState.error.localizedMessage
            }
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                btnTry.isVisible = loadState !is LoadState.Loading
                tvError.isVisible = loadState !is LoadState.Loading
            }
        }
    }
}