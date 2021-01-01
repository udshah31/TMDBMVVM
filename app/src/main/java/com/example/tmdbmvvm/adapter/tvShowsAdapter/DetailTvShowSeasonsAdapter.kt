package com.example.tmdbmvvm.adapter.tvShowsAdapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.tmdbmvvm.databinding.ItemSeasonBinding
import com.example.tmdbmvvm.models.tvshowdetail.Season
import com.example.tmdbmvvm.util.Constants.Companion.IMAGE_URL_FRONT
import javax.inject.Inject

class DetailTvShowSeasonsAdapter @Inject constructor(
    private val glide: RequestManager
) :
    RecyclerView.Adapter<DetailTvShowSeasonsAdapter.ViewHolder>() {

    private var productionListCompany = emptyList<Season>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSeasonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = productionListCompany[position]
        holder.bind(currentItem)

    }

    override fun getItemCount(): Int {
        return productionListCompany.size
    }

    fun setData(list: List<Season>) {
        this.productionListCompany = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemSeasonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(details: Season) {
            binding.apply {
                val image = IMAGE_URL_FRONT + details.poster_path
                glide.load(image).into(seasonImg)

                seasonName.text = details.name
                seasonDate.text = details.air_date
                if (details.overview.isEmpty()) {
                    seasonOverview.visibility = View.GONE
                } else {
                    seasonOverview.visibility = View.VISIBLE
                    seasonOverview.text = details.overview
                }

                seasonNumber.text = "Season No : " + details.season_number.toString()
                seasonEpisode.text = "Episode : " + details.episode_count.toString()
            }
        }
    }
}