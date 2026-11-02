package com.duongnd.wallpaper.presentation.main.activity

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.duongnd.wallpaper.base.activity.BaseActivity
import com.duongnd.wallpaper.databinding.ActivitySearchBinding
import com.duongnd.wallpaper.presentation.main.adapter.ImageAdapter
import com.duongnd.wallpaper.presentation.main.viewmodel.ImageViewModel
import com.duongnd.wallpaper.presentation.preview.activity.PreviewActivity
import com.duongnd.wallpaper.utils.getQueryTextChangeStateFlow
import com.duongnd.wallpaper.utils.gone
import com.duongnd.wallpaper.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.milliseconds

@AndroidEntryPoint
class SearchActivity : BaseActivity<ActivitySearchBinding>() {
    override fun provideViewBinding(): ActivitySearchBinding =
        ActivitySearchBinding.inflate(layoutInflater)

    private var imageAdapter = ImageAdapter()

    private val viewModel: ImageViewModel by viewModels()


    override fun initViews() {
        super.initViews()
        viewBinding.rcvImg.adapter = imageAdapter
        viewBinding.searchBar.btnCloseSearch.setOnClickListener {
            viewBinding.searchBar.searchView.setQuery("", false)
            viewBinding.searchBar.searchView.clearFocus()
            finish()
        }
        imageAdapter.onFavoriteClick = {
            imageAdapter.changeFav(it)
            viewModel.insertImageDb(it)
        }

        imageAdapter.onClick = {
            PreviewActivity.start(this@SearchActivity, it)
        }

        setupSearchView()

        viewBinding.searchBar.searchView.isIconified = false
        viewBinding.searchBar.searchView.requestFocus()
    }

    override fun initData() {
        viewModel.getListImage()
    }

    override fun initObserver() {
        viewModel.listImage.onEach { list ->
            if (list.isEmpty()) {
                viewBinding.rcvImg.gone()
                viewBinding.emtyList.root.visible()
            } else {
                viewBinding.rcvImg.visible()
                viewBinding.emtyList.root.gone()
                imageAdapter.setData(ArrayList(list))
            }
        }.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED).launchIn(lifecycleScope)
    }

    @OptIn(FlowPreview::class)
    private fun setupSearchView() {
        viewBinding.searchBar.searchView.getQueryTextChangeStateFlow()
            .debounce(300.milliseconds)
            .distinctUntilChanged()
            .onEach {
                viewModel.searchImage(it)
            }
            .launchIn(lifecycleScope)
    }

    companion object {
        fun start(context: Context) {
            Intent(context, SearchActivity::class.java).also {
                context.startActivity(it)
            }
        }
    }
}