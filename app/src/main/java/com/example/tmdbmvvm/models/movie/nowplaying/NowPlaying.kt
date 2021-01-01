package com.example.tmdbmvvm.models.movie.nowplaying

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NowPlaying(
    val dates: Dates,
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
) : Parcelable