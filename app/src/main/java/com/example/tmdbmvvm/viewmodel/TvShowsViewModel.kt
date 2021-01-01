package com.example.tmdbmvvm.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.tmdbmvvm.models.images.SliderImages
import com.example.tmdbmvvm.models.tvshowdetail.TvShowDetail
import com.example.tmdbmvvm.repository.TvShowsRepository
import com.example.tmdbmvvm.util.Constants.Companion.API_KEY
import com.example.tmdbmvvm.util.Constants.Companion.LANGUAGE
import com.example.tmdbmvvm.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class TvShowsViewModel @ViewModelInject constructor(
    private val repository: TvShowsRepository
) : ViewModel() {

    val tvAiringToday = repository.getTvAiringToday().cachedIn(viewModelScope)

    val tvOnTheAir = repository.getTvOnTheAir().cachedIn(viewModelScope)

    val tvPopular = repository.getTvPopular().cachedIn(viewModelScope)

    val tvTopRated = repository.getTvTopRated().cachedIn(viewModelScope)

    val tvImages: MutableLiveData<Resource<SliderImages>> = MutableLiveData()
    val imgLang = "en"

    val tvShowDetails: MutableLiveData<Resource<TvShowDetail>> = MutableLiveData()


    fun getTvImages(id: Int) = viewModelScope.launch {
        tvImages.postValue(Resource.Loading())
        val response = repository.getTvShowImage(id, API_KEY, LANGUAGE, imgLang)
        tvImages.postValue(handleTvImagesResponse(response))
    }

    private fun handleTvImagesResponse(response: Response<SliderImages>): Resource<SliderImages> {
        if (response.isSuccessful) {
            response.body()?.let { responseResult ->
                return Resource.Success(responseResult)
            }
        }
        return Resource.Error(response.message())
    }

    fun getTvShowDetail(id: Int) = viewModelScope.launch {
        tvShowDetails.postValue(Resource.Loading())
        val response = repository.getTvShowsDetail(id, API_KEY, LANGUAGE)
        tvShowDetails.postValue(handleTvShowDetailResponse(response))
    }


    private fun handleTvShowDetailResponse(response: Response<TvShowDetail>): Resource<TvShowDetail> {
        if (response.isSuccessful) {
            response.body()?.let { responseResults ->
                return Resource.Success(responseResults)
            }
        }
        return Resource.Error(response.message())
    }

}

