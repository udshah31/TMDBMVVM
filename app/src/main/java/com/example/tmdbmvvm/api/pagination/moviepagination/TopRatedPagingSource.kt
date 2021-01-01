package com.example.tmdbmvvm.api.pagination.moviepagination

import androidx.paging.PagingSource
import com.example.tmdbmvvm.api.ApiService
import com.example.tmdbmvvm.models.movie.topRated.Result
import com.example.tmdbmvvm.util.Constants.Companion.API_KEY
import com.example.tmdbmvvm.util.Constants.Companion.INDEX_PAGE
import com.example.tmdbmvvm.util.Constants.Companion.LANGUAGE
import retrofit2.HttpException
import java.io.IOException

class TopRatedPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, Result>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        val position = params.key ?: INDEX_PAGE

        return try {
            val response = apiService.getTopRated(API_KEY, LANGUAGE, position)
            val nowPlaying = response.results

            LoadResult.Page(
                data = nowPlaying,
                prevKey = if (position == INDEX_PAGE) null else position - 1,
                nextKey = if (nowPlaying.isEmpty()) null else position + 1
            )

        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }

    }
}