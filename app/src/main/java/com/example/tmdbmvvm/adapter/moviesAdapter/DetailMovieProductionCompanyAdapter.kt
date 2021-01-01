package com.example.tmdbmvvm.adapter.moviesAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.tmdbmvvm.databinding.ItemProductionBinding
import com.example.tmdbmvvm.models.moviedetail.ProductionCompany
import com.example.tmdbmvvm.util.Constants.Companion.IMAGE_URL_FRONT
import javax.inject.Inject

class DetailMovieProductionCompanyAdapter @Inject constructor(
    private val glide: RequestManager
) :
    RecyclerView.Adapter<DetailMovieProductionCompanyAdapter.ViewHolder>() {

    var productionListCompany = emptyList<ProductionCompany>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemProductionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = productionListCompany[position]
        holder.bind(currentItem)

    }

    override fun getItemCount(): Int {
        return productionListCompany.size
    }

    fun setData(list: List<ProductionCompany>) {
        this.productionListCompany = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemProductionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(details: ProductionCompany) {
            binding.apply {

                imgImage.visibility = View.VISIBLE

                val image = IMAGE_URL_FRONT + details.logo_path
                glide.load(image).into(imgImage)

                tvName.text = details.name + "(" + details.origin_country + ")"
            }
        }
    }
}