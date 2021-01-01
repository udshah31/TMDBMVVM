package com.example.tmdbmvvm.adapter.tvShowsAdapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.tmdbmvvm.R
import com.example.tmdbmvvm.databinding.ItemRowBinding
import com.example.tmdbmvvm.models.tvshows.popular.Result
import com.example.tmdbmvvm.util.Constants.Companion.IMAGE_URL_FRONT
import javax.inject.Inject

class TvPopularAdapter @Inject constructor(
    private val glide: RequestManager
) : PagingDataAdapter<Result, TvPopularAdapter.TvPopularViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TvPopularAdapter.TvPopularViewHolder {
        return TvPopularViewHolder(
            ItemRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: TvPopularAdapter.TvPopularViewHolder,
        position: Int
    ) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }


    inner class TvPopularViewHolder(private val itemRowBinding: ItemRowBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root) {

        init {
            itemRowBinding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        onItemClickListener?.let { it(item) }
                    }
                }

            }
        }

        fun bind(tvPopular: Result) {
            itemRowBinding.apply {
                val imageUrl = IMAGE_URL_FRONT + tvPopular.poster_path
                glide.load(imageUrl).into(image)

                if (tvPopular.vote_average <= 7) {
                    circularRatingBar.finishedStrokeColor = Color.parseColor("#D2D531")
                    circularRatingBar.unfinishedStrokeColor = Color.parseColor("#B3B44F")
                    circularRatingBar.progress = tvPopular.vote_average.toFloat() * 10
                } else {
                    circularRatingBar.finishedStrokeColor = Color.parseColor("#5FD27E")
                    circularRatingBar.unfinishedStrokeColor = Color.parseColor("#90cea1")
                    circularRatingBar.progress = tvPopular.vote_average.toFloat() * 10
                }

                tvTitle.text = tvPopular.original_name
                tvDate.text = tvPopular.first_air_date

            }
        }
    }

    private var onItemClickListener: ((Result) -> Unit)? = null
    fun setOnItemClickListener(listener: (Result) -> Unit) {
        onItemClickListener = listener
    }


    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem == newItem
            }
        }
    }


}