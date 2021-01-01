package com.example.tmdbmvvm.models.movie.popular

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Popular(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
) : Parcelable