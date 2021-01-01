package com.example.tmdbmvvm.models.tvshows.tvairingtoday

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TvAiringToday(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
) : Parcelable