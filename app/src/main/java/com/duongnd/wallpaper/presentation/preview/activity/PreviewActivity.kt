package com.duongnd.wallpaper.presentation.preview.activity

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.duongnd.wallpaper.R
import com.duongnd.wallpaper.base.activity.BaseActivity
import com.duongnd.wallpaper.databinding.ActivityPreviewBinding
import com.duongnd.wallpaper.domain.model.ImageEntity
import com.duongnd.wallpaper.domain.model.SaveImageState
import com.duongnd.wallpaper.domain.model.ShareImageState
import com.duongnd.wallpaper.domain.model.WallpaperState
import com.duongnd.wallpaper.domain.model.WallpaperTarget
import com.duongnd.wallpaper.presentation.dialog.SetWallpaperDialog
import com.duongnd.wallpaper.presentation.preview.viewmodel.PreviewViewModel
import com.duongnd.wallpaper.utils.NetworkConnectivityUtils
import com.duongnd.wallpaper.utils.Permission
import com.duongnd.wallpaper.utils.runWithPermissionChecker
import com.duongnd.wallpaper.utils.shareImage
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

        btnDown.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Trên Android 10 trở lên, không cần quyền WRITE_EXTERNAL_STORAGE để tải ảnh vào public thư mục
                viewModel.downloadImage()
            } else {
                runWithPermissionChecker(
                    permission = Permission.WRITE_EXTERNAL_STORAGE,
                    isRequestPermission = true,
                    onPermissionGranted = {
                        viewModel.downloadImage()
                    },
                    onPermissionDeny = {
                        showToast(getString(R.string.permission_message))
                    }
                )
            }
        }

        btnShare.setOnClickListener {
            val drawable = ivPreview.drawable
            val bitmap = (drawable as? android.graphics.drawable.BitmapDrawable)?.bitmap
            if (bitmap != null) {
                viewModel.shareImage(imgRes, bitmap)
            } else {
                Log.d("TAG", "initViewsPrv: ${ivPreview.drawable} & $bitmap")
            }
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
                is WallpaperState.Idle -> {}
                is WallpaperState.Loading -> showLoading()
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
        }.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED).launchIn(lifecycleScope)

        viewModel.saveImageState.onEach { state ->
            when (state) {
                is SaveImageState.Idle -> {}
                is SaveImageState.Loading -> showLoading()
                is SaveImageState.Success -> {
                    hideLoading()
                    showToast("Downloaded successfully")
                }

                is SaveImageState.Error -> {
                    hideLoading()
                    showToast(state.message)
                }
            }
            viewModel.resetSaveImageState()
        }.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED).launchIn(lifecycleScope)

        viewModel.shareImageUri.onEach { state ->
            when (state) {
                is ShareImageState.Error -> {
                    hideLoading()
                    showToast(state.message)
                    viewModel.resetSaveImageState()
                    Log.d("TAG", "initObserver: ${state.message}")
                }
                ShareImageState.Idle -> {}
                ShareImageState.Loading -> showLoading()
                is ShareImageState.Success -> {
                    hideLoading()
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "image/png"
                        putExtra(Intent.EXTRA_STREAM, state.uri)
                        clipData = ClipData.newUri(contentResolver, "Wallpaper", state.uri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    startActivity(Intent.createChooser(intent, getString(R.string.share)))
                }
            }
        }.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED).launchIn(lifecycleScope)

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
