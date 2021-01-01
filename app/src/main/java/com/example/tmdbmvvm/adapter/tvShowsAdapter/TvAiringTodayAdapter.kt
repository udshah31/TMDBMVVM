package com.example.tmdbmvvm.adapter.tvShowsAdapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.tmdbmvvm.databinding.ItemRowBinding
import com.example.tmdbmvvm.models.tvshows.tvairingtoday.Result
import com.example.tmdbmvvm.util.Constants.Companion.IMAGE_URL_FRONT
import javax.inject.Inject


class TvAiringTodayAdapter  @Inject constructor(
    private val glide: RequestManager
): PagingDataAdapter<Result, TvAiringTodayAdapter.TvAiringTodayViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TvAiringTodayAdapter.TvAiringTodayViewHolder {
        return TvAiringTodayViewHolder(
            ItemRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: TvAiringTodayAdapter.TvAiringTodayViewHolder,
        position: Int
    ) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }


    inner class TvAiringTodayViewHolder(private val itemRowBinding: ItemRowBinding) :
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

        fun bind(tvTodayAiring: Result) {
            itemRowBinding.apply {
                val imageUrl = IMAGE_URL_FRONT + tvTodayAiring.poster_path
                glide.load(imageUrl).into(image)

                if (tvTodayAiring.vote_average <= 7) {
                    circularRatingBar.finishedStrokeColor = Color.parseColor("#D2D531")
                    circularRatingBar.unfinishedStrokeColor = Color.parseColor("#B3B44F")
                    circularRatingBar.progress = tvTodayAiring.vote_average.toFloat() * 10
                } else {
                    circularRatingBar.finishedStrokeColor = Color.parseColor("#5FD27E")
                    circularRatingBar.unfinishedStrokeColor = Color.parseColor("#90cea1")
                    circularRatingBar.progress = tvTodayAiring.vote_average.toFloat() * 10
                }

                tvTitle.text = tvTodayAiring.original_name
                tvDate.text = tvTodayAiring.first_air_date

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