package com.example.tmdbmvvm.ui.movieFragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tmdbmvvm.R
import com.example.tmdbmvvm.adapter.moviesAdapter.LoadStateAdapter
import com.example.tmdbmvvm.adapter.moviesAdapter.TopRatedAdapter
import com.example.tmdbmvvm.databinding.TopratedFragmentBinding
import com.example.tmdbmvvm.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class TopRatedFragment : Fragment(R.layout.toprated_fragment) {

    private var _binding: TopratedFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MoviesViewModel>()

    @Inject
    lateinit var adapter: TopRatedAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = TopratedFragmentBinding.bind(view)

        binding.apply {
            rvTopRated.setHasFixedSize(true)
            rvTopRated.adapter = adapter.withLoadStateHeaderAndFooter(
                header = LoadStateAdapter { adapter.retry() },
                footer = LoadStateAdapter { adapter.retry() }
            )
            rvTopRated.layoutManager =
                GridLayoutManager(activity, 3, LinearLayoutManager.VERTICAL, false)

            adapter.setOnItemClickListener { result ->
                val bundle = Bundle().apply {
                    putParcelable("topRated", result)
                }
                findNavController().navigate(
                    R.id.action_nav_movie_to_detailTopRatedFragment,
                    bundle
                )
            }

            swipeRefresh.setOnRefreshListener {
                adapter.refresh()
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadState ->
                binding.swipeRefresh.isRefreshing = loadState.refresh is LoadState.Loading
            }
        }

        viewModel.topRated.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}