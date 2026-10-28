package com.duongnd.wallpaper.presentation.favorite.activity

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.duongnd.wallpaper.R
import com.duongnd.wallpaper.base.activity.BaseActivity
import com.duongnd.wallpaper.presentation.favorite.viewmodel.FavoriteViewModel
import com.duongnd.wallpaper.presentation.main.adapter.ImageAdapter
import com.duongnd.wallpaper.presentation.preview.activity.PreviewActivity
import com.duongnd.wallpaper.databinding.ActivityFavoriteBinding
import com.duongnd.wallpaper.utils.gone
import com.duongnd.wallpaper.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FavoriteActivity : BaseActivity<ActivityFavoriteBinding>() {

    override fun provideViewBinding(): ActivityFavoriteBinding =
        ActivityFavoriteBinding.inflate(layoutInflater)

    private val viewModel: FavoriteViewModel by viewModels()
    private var imageAdapter = ImageAdapter()

    override fun initViews() = with(viewBinding) {
        super.initViews()
        toolBar.btnBack.visible()
        toolBar.btnFav.gone()
        toolBar.btnSetting.gone()
        toolBar.tvTitle.text = getString(R.string.favorite)

        toolBar.btnBack.setOnClickListener {
            finish()
        }
        rcvImgFav.adapter = imageAdapter

        imageAdapter.onFavoriteClick = {
            imageAdapter.changeFav(it)
            viewModel.removeFav(it)
        }

        imageAdapter.onClick = {
            PreviewActivity.start(this@FavoriteActivity, it)
        }
    }

    override fun initData() {
        viewModel.getListDB()
    }

    override fun initObserver() {
        viewModel.listFavorite.onEach { list ->
            Log.d("FavoriteActivity", "Favorite list size: ${list.size}")
            if (list.isEmpty()) {
                viewBinding.rcvImgFav.gone()
                viewBinding.emtyList.root.visible()
            } else {
                viewBinding.rcvImgFav.visible()
                viewBinding.emtyList.root.gone()
                imageAdapter.setData(ArrayList(list))
            }
        }.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED).launchIn(lifecycleScope)
    }

    companion object {
        fun start(context: Context) {
            Intent(context, FavoriteActivity::class.java).also {
                context.startActivity(it)
            }
        }
    }
}
