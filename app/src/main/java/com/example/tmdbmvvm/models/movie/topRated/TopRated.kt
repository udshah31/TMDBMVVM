package com.example.tmdbmvvm.models.movie.topRated

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopRated(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
) : Parcelable