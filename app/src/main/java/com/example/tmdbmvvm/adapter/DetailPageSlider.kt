package com.example.tmdbmvvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.tmdbmvvm.databinding.ItemContainerBinding
import com.example.tmdbmvvm.models.images.Poster
import com.example.tmdbmvvm.util.Constants
import javax.inject.Inject

class DetailPageSlider @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<DetailPageSlider.SliderViewHolder>() {

    private var imageList = emptyList<Poster>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(
            ItemContainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val currentImage = imageList[position]
        holder.bind(currentImage)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun setData(posters: List<Poster>) {
        this.imageList = posters
        notifyDataSetChanged()
    }

    inner class SliderViewHolder(private val binding: ItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(poster: Poster) {
            binding.apply {
                val imageUrl = Constants.IMAGE_URL_BACK + poster.file_path
                glide.load(imageUrl).into(image)
            }
        }
    }

}