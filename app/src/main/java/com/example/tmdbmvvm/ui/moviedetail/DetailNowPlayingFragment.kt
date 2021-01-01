package com.example.tmdbmvvm.ui.moviedetail

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.RequestManager
import com.example.tmdbmvvm.HomeActivity
import com.example.tmdbmvvm.R
import com.example.tmdbmvvm.adapter.DetailPageSlider
import com.example.tmdbmvvm.adapter.moviesAdapter.DetailMovieProductionCompanyAdapter
import com.example.tmdbmvvm.adapter.moviesAdapter.DetailMovieProductionCountriesAdapter
import com.example.tmdbmvvm.databinding.MovieDetailFragmentBinding
import com.example.tmdbmvvm.models.movie.nowplaying.Result
import com.example.tmdbmvvm.util.Constants.Companion.IMAGE_URL_FRONT
import com.example.tmdbmvvm.util.Resource
import com.example.tmdbmvvm.viewmodel.MoviesViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailNowPlayingFragment : Fragment(R.layout.movie_detail_fragment) {

    private var _binding: MovieDetailFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MoviesViewModel>()

    @Inject
    lateinit var viewPagerAdapter: DetailPageSlider

    @Inject
    lateinit var adapterMovieProductionCompany: DetailMovieProductionCompanyAdapter

    @Inject
    lateinit var adapterMovieProductionCountry: DetailMovieProductionCountriesAdapter

    @Inject
    lateinit var glide: RequestManager


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = MovieDetailFragmentBinding.bind(view)

        (requireActivity() as HomeActivity).apply {
            supportActionBar?.hide()
            binding.bottomNavigationView.visibility = View.GONE
        }

        setHasOptionsMenu(true)

        val movie = arguments?.getParcelable<Result>("movie")

        binding.apply {
            sliderViewPager.adapter = viewPagerAdapter
            sliderViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setCurrentIndicators(position)
                }
            }
            )


            val backPoster = IMAGE_URL_FRONT + movie?.backdrop_path
            glide.load(backPoster).into(backgroundPoster)

            val frontPoster = IMAGE_URL_FRONT + movie?.poster_path
            glide.load(frontPoster).into(posterImage)

            imageBack.setOnClickListener {
                activity?.onBackPressed()
            }

            tvName.text = movie?.original_title

            rvProductionCompanies.adapter = adapterMovieProductionCompany
            rvProductionCompanies.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            rvProductionCountries.adapter = adapterMovieProductionCountry
            rvProductionCountries.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        }

        viewModel.getImages(movie!!.id)
        viewModel.getMovieDetails(movie.id)
        viewModel.images.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    response.data?.let { imageResponse ->
                        viewPagerAdapter.setData(imageResponse.posters.toList())
                        setIndicator(imageResponse.posters.size)
                    }
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    response.message?.let { message ->
                        Snackbar.make(
                            requireView(),
                            "An error occurred: $message ",
                            Snackbar.LENGTH_LONG
                        )
                            .show()
                    }
                }

                is Resource.Loading -> {
                   binding.progressBar.visibility = View.VISIBLE
                }
            }

        })


        viewModel.movieDetails.observe(viewLifecycleOwner, { response ->

            when (response) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    response.data?.let { movieDetails ->
                        binding.tvReleasedDate.text = movieDetails.release_date

                        if (movieDetails.spoken_languages.isEmpty()) {
                            binding.tvLang.text =
                                "Language : " + "N/A"
                        } else {
                            binding.tvLang.text =
                                "Language : " + movieDetails.spoken_languages.map { it.name }
                        }

                        binding.tvStatus.text = "Status : " + movieDetails.status
                        binding.tvTagLine.text = movieDetails.tagline
                        binding.tvOverview.text = movieDetails.overview
                        binding.tvReadMore.setOnClickListener {
                            if (binding.tvReadMore.text == "Read More...") {
                                binding.tvOverview.maxLines = Int.MAX_VALUE
                                binding.tvOverview.ellipsize = null
                                binding.tvReadMore.text = "Read Less"
                            } else {
                                binding.tvOverview.maxLines = 4
                                binding.tvOverview.ellipsize = TextUtils.TruncateAt.END
                                binding.tvReadMore.text = "Read More..."
                            }
                        }

                        if (movieDetails.vote_average <= 7) {
                            binding.circularRatingBar.finishedStrokeColor =
                                Color.parseColor("#D2D531")
                            binding.circularRatingBar.unfinishedStrokeColor =
                                Color.parseColor("#d2d531")
                            binding.circularRatingBar.progress =
                                movieDetails.vote_average.toFloat() * 10
                        } else {
                            binding.circularRatingBar.finishedStrokeColor =
                                Color.parseColor("#90cea1")
                            binding.circularRatingBar.unfinishedStrokeColor =
                                Color.parseColor("#21d07a")
                            binding.circularRatingBar.progress =
                                movieDetails.vote_average.toFloat() * 10
                        }

                        if (movieDetails.genres.isEmpty()) {
                            binding.tvGenre.text = "N/A"
                        } else {
                            binding.tvGenre.text = movieDetails.genres.map { it.name }.toString()
                        }

                        binding.tvRunTime.text = movieDetails.runtime.toString() + "min"

                        binding.tvBudget.text = "$ " + movieDetails.budget.toString()
                        binding.tvRevenue.text = "$ " + movieDetails.revenue.toString()

                        adapterMovieProductionCompany.setData(movieDetails.production_companies.toList())
                        adapterMovieProductionCountry.setData(movieDetails.production_countries.toList())

                        binding.apply {
                            backgroundPoster.visibility = View.VISIBLE
                            sliderViewPager.visibility = View.VISIBLE
                            imageBack.visibility = View.VISIBLE
                            posterImage.visibility = View.VISIBLE
                            tvName.visibility = View.VISIBLE
                            tvLang.visibility = View.VISIBLE
                            tvStatus.visibility = View.VISIBLE
                            tvReleasedDate.visibility = View.VISIBLE
                            tvTagLine.visibility = View.VISIBLE
                            titleOverview.visibility = View.VISIBLE
                            tvOverview.visibility = View.VISIBLE
                            tvReadMore.visibility = View.VISIBLE
                            layout1.visibility = View.VISIBLE
                            layout2.visibility = View.VISIBLE
                            tvProductions.visibility = View.VISIBLE
                            rvProductionCompanies.visibility = View.VISIBLE
                            tvProductionsCountries.visibility = View.VISIBLE
                            rvProductionCountries.visibility = View.VISIBLE
                            btnWebsite.visibility = View.VISIBLE
                            btnTrailer.visibility = View.VISIBLE
                        }
                    }
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    response.message?.let { message ->
                        Snackbar.make(
                            requireView(),
                            "An error occurred: $message ",
                            Snackbar.LENGTH_LONG
                        )
                            .show()
                    }
                }

                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }

        })


    }


    private fun setIndicator(count: Int) {
        val indicators = arrayOfNulls<ImageView>(count)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(requireContext())
            indicators[i]!!.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(), R.drawable.indicator_inactive
                )
            )
            indicators[i]!!.layoutParams = layoutParams
            binding.layoutSliderIndicator.addView(indicators[i])
        }
        binding.layoutSliderIndicator.visibility = View.VISIBLE
        setCurrentIndicators(0)
    }


    private fun setCurrentIndicators(position: Int) {
        val childCount: Int = binding.layoutSliderIndicator.childCount
        for (i in 0 until childCount) {
            val imageView = binding.layoutSliderIndicator.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_inactive
                    )
                )
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        (requireActivity() as HomeActivity).apply {
            supportActionBar?.show()
            binding.bottomNavigationView.visibility = View.VISIBLE
        }
    }
}