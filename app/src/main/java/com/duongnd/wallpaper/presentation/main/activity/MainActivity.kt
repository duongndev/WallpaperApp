package com.duongnd.wallpaper.presentation.main.activity

import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.duongnd.wallpaper.BuildConfig
import com.duongnd.wallpaper.base.activity.BaseActivity
import com.duongnd.wallpaper.databinding.ActivityMainBinding
import com.duongnd.wallpaper.presentation.dialog.ForceUpdateDialog
import com.duongnd.wallpaper.presentation.favorite.activity.FavoriteActivity
import com.duongnd.wallpaper.presentation.main.adapter.ImageAdapter
import com.duongnd.wallpaper.presentation.main.viewmodel.ImageViewModel
import com.duongnd.wallpaper.presentation.preview.activity.PreviewActivity
import com.duongnd.wallpaper.presentation.setting.SettingActivity
import com.duongnd.wallpaper.utils.LocalConfig
import com.duongnd.wallpaper.utils.Utils
import com.duongnd.wallpaper.utils.getQueryTextChangeStateFlow
import com.duongnd.wallpaper.utils.gone
import com.duongnd.wallpaper.utils.visible
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun provideViewBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    private var imageAdapter = ImageAdapter()

    private val viewModel: ImageViewModel by viewModels()

    override fun initViews() = with(viewBinding) {
        super.initViews()
        rcvImg.adapter = imageAdapter
        toolBar.btnSearch.visible()
        imageAdapter.onFavoriteClick = {
            imageAdapter.changeFav(it)
            viewModel.insertImageDb(it)
        }

        imageAdapter.onClick = {
            PreviewActivity.start(this@MainActivity, it)
        }

        toolBar.btnFav.setOnClickListener {
            FavoriteActivity.start(this@MainActivity)
        }
        toolBar.btnSetting.setOnClickListener {
            SettingActivity.start(this@MainActivity)
        }

        toolBar.btnSearch.setOnClickListener {
            // Ẩn thanh header chính
            toolBar.tvTitle.gone()
            toolBar.btnBack.gone()
            toolBar.btnSearch.gone()
            toolBar.btnFav.gone()
            toolBar.btnSetting.gone()
            rcvCate.gone()

            // Hiện khung tìm kiếm và nút đóng
            toolBar.searchView.visible()
            toolBar.btnCloseSearch.visible()

            // Tự động focus và bật bàn phím
            toolBar.searchView.isIconified = false
            toolBar.searchView.requestFocus()
        }

        toolBar.btnCloseSearch.setOnClickListener {

            toolBar.searchView.setQuery("", false)
            toolBar.searchView.clearFocus()

            // Ẩn khung tìm kiếm và nút đóng
            toolBar.searchView.gone()
            toolBar.btnCloseSearch.gone()

            toolBar.tvTitle.visible()
            toolBar.btnSearch.visible()
            toolBar.btnFav.visible()
            toolBar.btnSetting.visible()
            rcvCate.visible()
        }
        setupSearchView()

    }

    @OptIn(FlowPreview::class)
    private fun setupSearchView() {
        viewBinding.toolBar.searchView.getQueryTextChangeStateFlow()
            .debounce(300.milliseconds)
            .distinctUntilChanged()
            .onEach {
                viewModel.searchImage(it)
            }
            .launchIn(lifecycleScope)
    }


    private fun setupCategories(categories: List<String>) = with(viewBinding) {
        categories.forEach { category ->
            rcvCate.addTab(rcvCate.newTab().setText(category))
        }

        rcvCate.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val category = categories[tab?.position ?: 0]
                viewModel.filterByCategory(category)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    override fun initData() {
//        checkForceUpdate()
        viewModel.getListImage()
    }

    private fun checkForceUpdate() {
        val currentVersion = BuildConfig.VERSION_NAME
        val minRequiredVersion = LocalConfig.minRequiredVersion
        val isForceFlag = LocalConfig.isForceUpdateEnabled
        val isVersionOutdated = Utils.isVersionLower(currentVersion, minRequiredVersion)

        if (isForceFlag || isVersionOutdated) {
            if (!isFinishing && !isDestroyed) {
                ForceUpdateDialog(this@MainActivity).apply {
                    onExit = {
                        finishAffinity()
                    }
                    show()
                }
            }
        }
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

        viewModel.listCategoryState.onEach { list ->
            if (list.isEmpty()) {
                viewBinding.rcvCate.gone()
            } else {
                viewBinding.rcvCate.visible()
                setupCategories(list)
            }
        }.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED).launchIn(lifecycleScope)


    }
}
