package com.duongnd.wallpaper.component.main.activity

import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.duongnd.wallpaper.BuildConfig
import com.duongnd.wallpaper.base.activity.BaseActivity
import com.duongnd.wallpaper.component.favorite.activity.FavoriteActivity
import com.duongnd.wallpaper.component.main.adapter.ImageAdapter
import com.duongnd.wallpaper.component.main.viewmodel.ImageViewModel
import com.duongnd.wallpaper.component.preview.activity.PreviewActivity
import com.duongnd.wallpaper.component.setting.SettingActivity
import com.duongnd.wallpaper.databinding.ActivityMainBinding
import com.duongnd.wallpaper.dialog.ForceUpdateDialog
import com.duongnd.wallpaper.utils.LocalConfig
import com.duongnd.wallpaper.utils.Utils
import com.duongnd.wallpaper.utils.gone
import com.duongnd.wallpaper.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun provideViewBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    private var imageAdapter = ImageAdapter()

    private val viewModel: ImageViewModel by viewModels()

    override fun initViews() = with(viewBinding) {
        super.initViews()
        rcvImg.adapter = imageAdapter
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
    }
}
