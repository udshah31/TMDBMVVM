package com.example.tmdbmvvm.ui.tvshowdetail

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
import com.example.tmdbmvvm.adapter.tvShowsAdapter.DetailTvShowProductionCompanyAdapter
import com.example.tmdbmvvm.adapter.tvShowsAdapter.DetailTvShowProductionCountriesAdapter
import com.example.tmdbmvvm.adapter.tvShowsAdapter.DetailTvShowSeasonsAdapter
import com.example.tmdbmvvm.databinding.TvDetailFragmentBinding
import com.example.tmdbmvvm.models.tvshows.ontheair.Result
import com.example.tmdbmvvm.util.Constants.Companion.IMAGE_URL_FRONT
import com.example.tmdbmvvm.util.Resource
import com.example.tmdbmvvm.viewmodel.TvShowsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailTvOnTheAirFragment : Fragment(R.layout.tv_detail_fragment) {

    private var _binding: TvDetailFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<TvShowsViewModel>()

    @Inject
    lateinit var viewPagerAdapter: DetailPageSlider

    @Inject
    lateinit var adapterTvShowProductionCompany: DetailTvShowProductionCompanyAdapter

    @Inject
    lateinit var adapterTvShowProductionCountry: DetailTvShowProductionCountriesAdapter

    @Inject
    lateinit var adapterSeason: DetailTvShowSeasonsAdapter

    @Inject
    lateinit var glide: RequestManager


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = TvDetailFragmentBinding.bind(view)

        (requireActivity() as HomeActivity).apply {
            supportActionBar?.hide()
            binding.bottomNavigationView.visibility = View.GONE
        }

        setHasOptionsMenu(true)

        val tvOnTheAir = arguments?.getParcelable<Result>("tvOnTheAir")

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


            val backPoster = IMAGE_URL_FRONT + tvOnTheAir?.backdrop_path
            glide.load(backPoster).into(backgroundPoster)

            val frontPoster = IMAGE_URL_FRONT + tvOnTheAir?.poster_path
            glide.load(frontPoster).into(posterImage)

            imageBack.setOnClickListener {
                activity?.onBackPressed()
            }

            tvName.text = tvOnTheAir?.original_name

            rvProductionCompanies.adapter = adapterTvShowProductionCompany
            rvProductionCompanies.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            rvProductionCountries.adapter = adapterTvShowProductionCountry
            rvProductionCountries.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


            rvSeasons.adapter = adapterSeason
            rvSeasons.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        }

        viewModel.getTvImages(tvOnTheAir!!.id)
        viewModel.getTvShowDetail(tvOnTheAir.id)
        viewModel.tvImages.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    response.data?.let { imageResponse ->
                        viewPagerAdapter.setData(imageResponse.posters.toList())
                        setIndicator(imageResponse.posters.size)
                        binding.sliderViewPager.visibility = View.VISIBLE
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


        viewModel.tvShowDetails.observe(viewLifecycleOwner, { response ->

            when (response) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    response.data?.let { tvDetails ->
                        binding.tvReleasedDate.text = tvDetails.first_air_date
                        adapterSeason.setData(tvDetails.seasons.toList())

                        if (tvDetails.spoken_languages.isEmpty()) {
                            binding.tvLang.text =
                                "Language : " + "N/A"
                        } else {
                            binding.tvLang.text =
                                "Language : " + tvDetails.spoken_languages.map { it.name }
                        }

                        binding.tvStatus.text = "Status : " + tvDetails.status
                        binding.tvTagLine.text = tvDetails.tagline
                        binding.tvOverview.text = tvDetails.overview
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

                        if (tvDetails.vote_average <= 7) {
                            binding.circularRatingBar.finishedStrokeColor =
                                Color.parseColor("#D2D531")
                            binding.circularRatingBar.unfinishedStrokeColor =
                                Color.parseColor("#d2d531")
                            binding.circularRatingBar.progress =
                                tvDetails.vote_average.toFloat() * 10
                        } else {
                            binding.circularRatingBar.finishedStrokeColor =
                                Color.parseColor("#90cea1")
                            binding.circularRatingBar.unfinishedStrokeColor =
                                Color.parseColor("#21d07a")
                            binding.circularRatingBar.progress =
                                tvDetails.vote_average.toFloat() * 10
                        }

                        if (tvDetails.genres.isEmpty()) {
                            binding.tvGenre.text = "N/A"
                        } else {
                            binding.tvGenre.text = tvDetails.genres.map { it.name }.toString()
                        }

                        binding.tvNumberOfEpisodes.text =
                            "Episodes : " + tvDetails.number_of_episodes
                        binding.tvNumberOfSeasons.text = "Seasons : " + tvDetails.number_of_seasons

                        binding.tvEpisodeRunTime.text =
                            tvDetails.episode_run_time.toString() + "min"

                        binding.tvInProduction.text = tvDetails.in_production.toString()
                        binding.tvLastAirDate.text = tvDetails.last_air_date

                        adapterTvShowProductionCompany.setData(tvDetails.production_companies.toList())
                        adapterTvShowProductionCountry.setData(tvDetails.production_countries.toList())

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
                            tvSeasons.visibility = View.VISIBLE
                            rvSeasons.visibility = View.VISIBLE
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