package com.example.tmdbmvvm.api

import com.example.tmdbmvvm.models.images.SliderImages
import com.example.tmdbmvvm.models.movie.nowplaying.NowPlaying
import com.example.tmdbmvvm.models.movie.popular.Popular
import com.example.tmdbmvvm.models.movie.topRated.TopRated
import com.example.tmdbmvvm.models.movie.upcoming.Upcoming
import com.example.tmdbmvvm.models.moviedetail.MovieDetails
import com.example.tmdbmvvm.models.tvshowdetail.TvShowDetail
import com.example.tmdbmvvm.models.tvshows.ontheair.OnTheAir
import com.example.tmdbmvvm.models.tvshows.popular.TvPopular
import com.example.tmdbmvvm.models.tvshows.toprated.TvTopRated
import com.example.tmdbmvvm.models.tvshows.tvairingtoday.TvAiringToday
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //nowPlaying movies
    @GET("3/movie/now_playing")
    suspend fun getNowPlaying(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ): NowPlaying


    //popular movies
    @GET("3/movie/popular")
    suspend fun getPopular(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ): Popular


    //Top Rated movies
    @GET("3/movie/top_rated")
    suspend fun getTopRated(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ): TopRated


    //Upcoming movies
    @GET("3/movie/upcoming")
    suspend fun getUpcoming(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ): Upcoming


    //TV Airing Today
    @GET("3/tv/airing_today")
    suspend fun getTvAiringToday(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ): TvAiringToday


    //Tv on the air
    @GET("3/tv/on_the_air")
    suspend fun getOnTheAir(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ): OnTheAir


    //Tv Popular
    @GET("3/tv/popular")
    suspend fun getTvPopular(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ): TvPopular


    //Tv Popular
    @GET("3/tv/top_rated")
    suspend fun getTvTopRated(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ): TvTopRated

    //MovieImages
    @GET("3/movie/{movie_id}/images")
    suspend fun getImages(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("include_image_language") imgLang: String,
    ): Response<SliderImages>


    //MoviesDetails
    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
    ): Response<MovieDetails>

    //TvShowImages
    @GET("3/tv/{tv_id}/images")
    suspend fun getTvShowImages(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("include_image_language") imgLang: String,
    ): Response<SliderImages>

    //TvShowDetails
    @GET("3/tv/{tv_id}")
    suspend fun getTvShowDetails(
        @Path("tv_id") tvId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
    ): Response<TvShowDetail>
}