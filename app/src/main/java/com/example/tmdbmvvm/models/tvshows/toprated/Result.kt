package com.example.tmdbmvvm.models.tvshows.toprated

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Result(

    val backdrop_path: @RawValue Any,
    val first_air_date: String,
    val genre_ids: List<Int>,
    val id: Int,
    val name: String,
    val origin_country: List<String>,
    val original_language: String,
    val original_name: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val vote_average: Double,
    val vote_count: Int
) : Parcelable