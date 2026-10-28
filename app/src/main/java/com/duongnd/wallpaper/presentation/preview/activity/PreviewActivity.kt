package com.duongnd.wallpaper.presentation.preview.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.duongnd.wallpaper.R
import com.duongnd.wallpaper.base.activity.BaseActivity
import com.duongnd.wallpaper.presentation.preview.viewmodel.PreviewViewModel
import com.duongnd.wallpaper.databinding.ActivityPreviewBinding
import com.duongnd.wallpaper.presentation.dialog.SetWallpaperDialog
import com.duongnd.wallpaper.domain.model.ImageEntity
import com.duongnd.wallpaper.domain.model.WallpaperState
import com.duongnd.wallpaper.domain.model.WallpaperTarget
import com.duongnd.wallpaper.utils.NetworkConnectivityUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class PreviewActivity : BaseActivity<ActivityPreviewBinding>() {

    override fun provideViewBinding(): ActivityPreviewBinding =
        ActivityPreviewBinding.inflate(layoutInflater)

    private val viewModel: PreviewViewModel by viewModels()

    override fun initViews() = with(viewBinding) {
        super.initViews()

        btnBack.setOnClickListener {
            finish()
        }

        val imgRes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_IMAGE, ImageEntity::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_IMAGE) as? ImageEntity
        }

        // Edge case handling: Empty or invalid image resource
        if (imgRes == null || imgRes.imgRes <= 0) {
            showToast(getString(R.string.emty_list_message))
            finish()
            return@with
        }

        ivPreview.setImageResource(imgRes.imgRes)
        viewModel.setInitialImage(imgRes)

        btnFav.setOnClickListener {
            viewModel.toggleFavorite(imgRes)
        }

        btnSetWallpaper.setOnClickListener {
            if (!NetworkConnectivityUtils.isConnected(this@PreviewActivity)) {
                showToast(getString(R.string.connection_lost_message))
                return@setOnClickListener
            }

            SetWallpaperDialog(
                context = this@PreviewActivity,
                onHomeScreenClick = {
                    viewModel.setWallpaper(WallpaperTarget.HOME)
                },
                onLockScreenClick = {
                    viewModel.setWallpaper(WallpaperTarget.LOCK)
                },
                onBothScreenClick = {
                    viewModel.setWallpaper(WallpaperTarget.BOTH)
                }
            ).show()
        }

    }


    override fun initObserver() {
        viewModel.imageF.onEach { image ->
            if (image == null) return@onEach
            if (image.isFavorite) {
                viewBinding.btnFav.setImageResource(R.drawable.ic_favorite_selected)
                viewBinding.btnFav.setColorFilter(ContextCompat.getColor(this, R.color.red))
            } else {
                viewBinding.btnFav.setImageResource(R.drawable.ic_favorite)
                viewBinding.btnFav.setColorFilter(Color.WHITE)
            }
        }.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).launchIn(lifecycleScope)

        viewModel.wallpaperStatus.onEach { state ->
            when (state) {
                is WallpaperState.Idle -> {
                    // Do nothing
                }

                is WallpaperState.Loading -> {
                    showLoading()
                }

                is WallpaperState.Success -> {
                    hideLoading()
                    showToast(getString(R.string.wallpaper_set_successfully))
                }

                is WallpaperState.Error -> {
                    hideLoading()
                    showToast(state.message)
                }
            }

            viewModel.resetWallpaperState()
        }.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).launchIn(lifecycleScope)
    }

    companion object {
        private const val EXTRA_IMAGE = "extra_image"

        fun start(context: Context, imgRes: ImageEntity) {
            Intent(context, PreviewActivity::class.java).also {
                it.putExtra(EXTRA_IMAGE, imgRes)
                context.startActivity(it)
            }
        }
    }
}
