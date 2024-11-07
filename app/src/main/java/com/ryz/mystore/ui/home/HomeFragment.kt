package com.ryz.mystore.ui.home

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ryz.mystore.R
import com.ryz.mystore.databinding.FragmentHomeBinding
import com.ryz.mystore.utils.BaseFragment
import com.ryz.mystore.utils.ImageType
import com.ryz.mystore.utils.collectUiState
import com.ryz.mystore.utils.loadImageUrl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private lateinit var productAdapter: ProductAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    private val viewModel by viewModels<HomeViewModel>()
    private var currentCategory: String? = null

    override fun initUI() {
        binding.ivBanner.loadImageUrl(
            url = getString(R.string.banner_url),
            progressBar = binding.bannerProgressBar,
            imageType = ImageType.ROUNDED
        )

        viewModel.getAllProduct()
        viewModel.getCategory()
    }

    override fun initListener() = with(binding) {
        swipeLayout.setOnRefreshListener {
            currentCategory?.let { category ->
                viewModel.getProductByCategory(category)
            } ?: run {
                viewModel.getAllProduct()
            }
            swipeLayout.isRefreshing = false
        }
    }

    override fun initAdapter() {
        with(binding.rvCategory) {
            categoryAdapter = CategoryAdapter { category ->
                if (currentCategory == category) {
                    viewModel.getAllProduct()
                    currentCategory = null
                } else {
                    viewModel.getProductByCategory(category)
                    currentCategory = category
                }
            }

            adapter = categoryAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        with(binding.rvProduct) {
            productAdapter = ProductAdapter { productId ->
                productId?.let { id ->
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToHomeDetailFragment(
                            id
                        )
                    )
                    currentCategory = null
                }
            }

            adapter = productAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    override suspend fun collectUiState(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            viewModel.uiState.collectUiState(
                fragment = this@HomeFragment,
                progressBar = binding.progressBar,
                onSuccess = { state ->
                    state.category?.let { data ->
                        categoryAdapter.submitList(data)
                    }

                    state.allProduct?.let { data ->
                        productAdapter.submitList(data)
                    }
                }
            )
        }
    }

    override fun onDestroyView() {
        binding.rvCategory.adapter = null
        binding.rvProduct.adapter = null
        super.onDestroyView()
    }
}