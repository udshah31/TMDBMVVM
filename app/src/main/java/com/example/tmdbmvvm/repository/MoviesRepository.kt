package com.example.tmdbmvvm.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.tmdbmvvm.api.ApiService
import com.example.tmdbmvvm.api.pagination.moviepagination.NowPlayingPagingSource
import com.example.tmdbmvvm.api.pagination.moviepagination.PopularPagingSource
import com.example.tmdbmvvm.api.pagination.moviepagination.TopRatedPagingSource
import com.example.tmdbmvvm.api.pagination.moviepagination.UpcomingPagingSource
import com.example.tmdbmvvm.util.Constants.Companion.MAX_PAGE_SIZE
import com.example.tmdbmvvm.util.Constants.Companion.QUERY_PAGE_SIZE
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(
    private val apiService: ApiService
) {
    fun getNowPlaying() = Pager(
        config = PagingConfig(
            pageSize = QUERY_PAGE_SIZE,
            maxSize = MAX_PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { NowPlayingPagingSource(apiService) }
    ).liveData


    fun getPopular() = Pager(
        config = PagingConfig(
            pageSize = QUERY_PAGE_SIZE,
            maxSize = MAX_PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { PopularPagingSource(apiService) }
    ).liveData

    fun getTopRated() = Pager(
        config = PagingConfig(
            pageSize = QUERY_PAGE_SIZE,
            maxSize = MAX_PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { TopRatedPagingSource(apiService) }
    ).liveData

    fun getUpcoming() = Pager(
        config = PagingConfig(
            pageSize = QUERY_PAGE_SIZE,
            maxSize = MAX_PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { UpcomingPagingSource(apiService) }
    ).liveData

    suspend fun getImages(
        id: Int,
        apiKey: String,
        lang: String,
        imgLang: String
    ) = apiService.getImages(id, apiKey, lang, imgLang)

    suspend fun getMovieDetails(
        id: Int,
        apiKey: String,
        lang: String,
    ) = apiService.getMovieDetails(id, apiKey, lang)
}