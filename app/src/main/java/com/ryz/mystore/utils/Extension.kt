package com.ryz.mystore.utils

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.ryz.mystore.R
import com.ryz.mystore.data.remote.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

fun Fragment.showMessage(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
}

fun String.toCapitalizeWords(): String {
    return split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { it.uppercase() }
    }
}

fun ImageView.loadImageUrl(
    url: String?,
    progressBar: ProgressBar? = null,
    imageType: ImageType = ImageType.DEFAULT
) {
    val glide = Glide.with(context)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>,
                isFirstResource: Boolean
            ): Boolean {
                progressBar?.visibility = View.GONE
                return false
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable>?,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                progressBar?.visibility = View.GONE
                return false
            }
        })
        .fitCenter()
        .timeout(10000)

    when (imageType) {
        ImageType.DEFAULT -> glide.into(this)
        ImageType.CIRCLE -> glide.circleCrop().into(this)
        ImageType.ROUNDED -> glide.transform(RoundedCorners(20)).into(this)
    }
}

fun Fragment.initToolbar(
    title: String,
    toolbar: MaterialToolbar,
    onNavigationUp: (() -> Unit)? = null
) {
    val toolbarColor = requireContext().getColor(R.color.black)
    val toolbarBackground = requireContext().getColor(R.color.white)

    toolbar.apply {
        this.title = title
        setNavigationOnClickListener {
            onNavigationUp?.invoke() ?: findNavController().navigateUp()
        }
        setTitleTextColor(toolbarColor)
        setNavigationIconTint(toolbarColor)
        setBackgroundColor(toolbarBackground)
    }
}

fun Fragment.setOnBackPressed(destinationId: Int, inclusive: Boolean = false) {
    val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // Handle the back button event
            findNavController().popBackStack(destinationId, inclusive)
        }
    }
    requireActivity().onBackPressedDispatcher.addCallback(
        viewLifecycleOwner,
        onBackPressedCallback
    )
}

fun <T, S> MutableStateFlow<GenericState<S>>.updateWithResource(
    resource: Resource<T>,
    state: (T?) -> S
) = update {
    when (resource) {
        is Resource.Loading -> GenericState.Loading(isLoading = resource.isLoading)
        is Resource.Success -> GenericState.Success(data = state(resource.data))
        is Resource.Error -> GenericState.Error(message = resource.error)
    }
}

suspend inline fun <T> StateFlow<GenericState<T>>.collectUiState(
    fragment: Fragment,
    progressBar: ProgressBar? = null,
    crossinline onLoading: (Boolean) -> Unit = {},
    crossinline onSuccess: (T) -> Unit
) {
    collect { state ->
        when (state) {
            is GenericState.Loading -> {
                progressBar?.isVisible = state.isLoading
                onLoading(state.isLoading)
            }

            is GenericState.Success -> state.data?.let(onSuccess)

            is GenericState.Error -> {
                state.message?.let { message ->
                    fragment.showMessage(message)
                    state.message = null
                }
            }
        }
    }
}