package com.example.tmdbmvvm.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.tmdbmvvm.models.images.SliderImages
import com.example.tmdbmvvm.models.moviedetail.MovieDetails
import com.example.tmdbmvvm.repository.MoviesRepository
import com.example.tmdbmvvm.util.Constants.Companion.API_KEY
import com.example.tmdbmvvm.util.Constants.Companion.LANGUAGE
import com.example.tmdbmvvm.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class MoviesViewModel @ViewModelInject constructor(
    private val repository: MoviesRepository
) : ViewModel() {

    val nowPlaying = repository.getNowPlaying().cachedIn(viewModelScope)

    val popular = repository.getPopular().cachedIn(viewModelScope)

    val topRated = repository.getTopRated().cachedIn(viewModelScope)

    val upcoming = repository.getUpcoming().cachedIn(viewModelScope)

    val images: MutableLiveData<Resource<SliderImages>> = MutableLiveData()
    val imgLang = "en"

    val movieDetails: MutableLiveData<Resource<MovieDetails>> = MutableLiveData()

    fun getImages(id: Int) = viewModelScope.launch {
        images.postValue(Resource.Loading())
        val response = repository.getImages(id, API_KEY, LANGUAGE, imgLang)
        images.postValue(handleImagesResponse(response))
    }

    private fun handleImagesResponse(response: Response<SliderImages>): Resource<SliderImages> {
        if (response.isSuccessful) {
            response.body()?.let { responseResult ->
                return Resource.Success(responseResult)
            }
        }
        return Resource.Error(response.message())
    }

    fun getMovieDetails(id: Int) = viewModelScope.launch {
        movieDetails.postValue(Resource.Loading())
        val response = repository.getMovieDetails(id, API_KEY, LANGUAGE)
        movieDetails.postValue(handleMovieDetailsResponse(response))
    }

    private fun handleMovieDetailsResponse(response: Response<MovieDetails>): Resource<MovieDetails> {
        if (response.isSuccessful) {
            response.body()?.let { responseResult ->
                return Resource.Success(responseResult)

            }
        }

        return Resource.Error(response.message())
    }


}