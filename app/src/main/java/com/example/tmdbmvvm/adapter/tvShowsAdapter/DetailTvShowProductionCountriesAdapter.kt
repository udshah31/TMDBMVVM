package com.example.tmdbmvvm.adapter.tvShowsAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdbmvvm.databinding.ItemProductionBinding
import com.example.tmdbmvvm.models.tvshowdetail.ProductionCountry
import javax.inject.Inject

class DetailTvShowProductionCountriesAdapter @Inject constructor() :
    RecyclerView.Adapter<DetailTvShowProductionCountriesAdapter.ViewHolder>() {

    var productionListCountries = emptyList<ProductionCountry>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemProductionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = productionListCountries[position]
        holder.bind(currentItem)

    }

    override fun getItemCount(): Int {
        return productionListCountries.size
    }

    fun setData(list: List<ProductionCountry>) {
        this.productionListCountries = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemProductionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(details: ProductionCountry) {
            binding.apply {
                imgImage.visibility = View.GONE
                tvName.text = details.name + "(" + details.iso_3166_1 + ")"
            }
        }
    }
}