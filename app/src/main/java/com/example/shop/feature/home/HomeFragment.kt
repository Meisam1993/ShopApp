package com.example.shop.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.services.data.dataclasses.Product
import com.example.shop.base.BaseFragment
import com.example.shop.databinding.FragmentHomeBinding
import com.example.shop.feature.home.banner.BannerAdapter
import com.example.shop.feature.home.product.ProductListAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {
    private val viewModel: HomeViewModel by viewModel()
    private val latestAdapter: ProductListAdapter by inject()
    private val popularAdapter: ProductListAdapter by inject()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLatestAdapter()
        initPopularAdapter()

        lifecycleScope.launch {
            viewModel.uiState.onEach {
                if (it.errorResponse == null) {
                    if (it.progressBar)
                        setProgressBarIndicator(true)
                    else
                        setProgressBarIndicator(false)
                    if (!it.banners.isNullOrEmpty()) {
                        //Banner
                        val bannerAdapter = BannerAdapter(this@HomeFragment, it.banners)
                        binding.bannerSlider.adapter = bannerAdapter
                        binding.dotsIndicator.attachTo(binding.bannerSlider)
                    }
                    if (!it.products.isNullOrEmpty()) {
                        //Product
                        latestAdapter.products = it.products as ArrayList<Product>
                        popularAdapter.products = it.products as ArrayList<Product>
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error: ${it.errorResponse.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }.collect()
        }
    }

    private fun initLatestAdapter() {
        binding.latestRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.latestRv.adapter = latestAdapter
    }

    private fun initPopularAdapter() {
        binding.popularRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.popularRv.adapter = popularAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}