package com.ryz.mystore.ui.home

import android.content.Intent
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ryz.mystore.R
import com.ryz.mystore.data.model.response.ProductResponse
import com.ryz.mystore.databinding.FragmentHomeDetailBinding
import com.ryz.mystore.utils.BaseFragment
import com.ryz.mystore.utils.collectUiState
import com.ryz.mystore.utils.initToolbar
import com.ryz.mystore.utils.loadImageUrl
import com.ryz.mystore.utils.toCapitalizeWords
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeDetailFragment :
    BaseFragment<FragmentHomeDetailBinding>(FragmentHomeDetailBinding::inflate) {
    private val args by navArgs<HomeDetailFragmentArgs>()
    private val viewModel by viewModels<HomeViewModel>()

    override fun initUI() {
        initToolbar(
            getString(R.string.details),
            binding.toolbarLayout.toolbar,
            isMenu = true,
            onMenu = {
                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                    val deepLinkUrl = "https://www.mystore.com/detail/${args.productId}"
                    putExtra(Intent.EXTRA_TEXT, deepLinkUrl)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, getString(R.string.share))
                startActivity(shareIntent)
            })

        viewModel.getProductDetail(args.productId)
    }

    override suspend fun collectUiState(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            viewModel.uiState.collectUiState(
                fragment = this@HomeDetailFragment,
                progressBar = binding.progressBar,
                onLoading = { isLoading ->
                    binding.clContent.isVisible = !isLoading
                },
                onSuccess = { state ->
                    state.productDetail?.let { data ->
                        populateUi(data)
                    }
                }
            )
        }
    }

    private fun populateUi(data: ProductResponse) = with(binding) {
        ivProduct.loadImageUrl(data.image)
        tvProductName.text = data.title
        tvCategory.text = data.category?.toCapitalizeWords()
        tvRating.text = data.rating?.rate.toString()
        tvCountRating.text = getString(R.string.count_rating, data.rating?.count.toString())
        tvDescription.text = data.description
        tvPrice.text = getString(R.string.format_price, data.price.toString())
    }
}