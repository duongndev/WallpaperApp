package com.duongnd.wallpaper.component.preview.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.duongnd.wallpaper.R
import com.duongnd.wallpaper.base.activity.BaseActivity
import com.duongnd.wallpaper.component.preview.viewmodel.PreviewViewModel
import com.duongnd.wallpaper.domain.model.ImageEntity
import com.duongnd.wallpaper.databinding.ActivityPreviewBinding
import com.duongnd.wallpaper.dialog.SetWallpaperDialog
import com.duongnd.wallpaper.domain.model.WallpaperState
import com.duongnd.wallpaper.domain.model.WallpaperTarget
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

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
        if (imgRes != null) {
            ivPreview.setImageResource(imgRes.imgRes)
            viewModel.setInitialImage(imgRes)
        }

        btnFav.setOnClickListener {
            if (imgRes != null) {
                viewModel.toggleFavorite(imgRes)
            }
        }
        
        btnSetWallpaper.setOnClickListener {
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
        viewModel.imageF.onEach {
            if (it == null) return@onEach
            Log.d("TAG", "initObserverPv: ${it.isFavorite}")
            if (it.isFavorite) {
                viewBinding.btnFav.setImageResource(R.drawable.ic_favorite_selected)
            } else {
                viewBinding.btnFav.setImageResource(R.drawable.ic_favorite)
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
                    showToast("Set wallpaper successfully")
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