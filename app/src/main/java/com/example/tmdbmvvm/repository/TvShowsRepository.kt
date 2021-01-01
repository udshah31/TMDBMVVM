package com.example.tmdbmvvm.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.tmdbmvvm.api.ApiService
import com.example.tmdbmvvm.api.pagination.tvshowspagination.TvAiringTodayPagingSource
import com.example.tmdbmvvm.api.pagination.tvshowspagination.TvOnTheAirPagingSource
import com.example.tmdbmvvm.api.pagination.tvshowspagination.TvPopularPagingSource
import com.example.tmdbmvvm.api.pagination.tvshowspagination.TvTopRatedPagingSource
import com.example.tmdbmvvm.util.Constants.Companion.MAX_PAGE_SIZE
import com.example.tmdbmvvm.util.Constants.Companion.QUERY_PAGE_SIZE
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvShowsRepository @Inject constructor(
    private val apiService: ApiService
) {

    fun getTvAiringToday() = Pager(
        config = PagingConfig(
            pageSize = QUERY_PAGE_SIZE,
            maxSize = MAX_PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { TvAiringTodayPagingSource(apiService) }
    ).liveData


    fun getTvOnTheAir() = Pager(
        config = PagingConfig(
            pageSize = QUERY_PAGE_SIZE,
            maxSize = MAX_PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { TvOnTheAirPagingSource(apiService) }
    ).liveData


    fun getTvPopular() = Pager(
        config = PagingConfig(
            pageSize = QUERY_PAGE_SIZE,
            maxSize = MAX_PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { TvPopularPagingSource(apiService) }
    ).liveData

    fun getTvTopRated() = Pager(
        config = PagingConfig(
            pageSize = QUERY_PAGE_SIZE,
            maxSize = MAX_PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { TvTopRatedPagingSource(apiService) }
    ).liveData

    suspend fun getTvShowImage(
        id: Int,
        apiKey: String,
        lang: String,
        imgLang: String
    ) = apiService.getTvShowImages(id, apiKey, lang, imgLang)


    suspend fun getTvShowsDetail(
        id: Int,
        apiKey: String,
        lang: String,
    ) = apiService.getTvShowDetails(id, apiKey, lang)
}