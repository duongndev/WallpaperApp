package com.duongnd.wallpaper.component.main

import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.duongnd.wallpaper.base.activity.BaseActivity
import com.duongnd.wallpaper.component.main.adapter.ImageAdapter
import com.duongnd.wallpaper.component.main.viewmodel.ImageViewModel
import com.duongnd.wallpaper.data.model.ImageEntity
import com.duongnd.wallpaper.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun provideViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    private var imageAdapter = ImageAdapter()

    private var selectedImage: ImageEntity? = null

    private val viewModel: ImageViewModel by viewModels()

    override fun initViews() = with(viewBinding) {
        super.initViews()
        rcvImg.adapter = imageAdapter
        imageAdapter.onClick = {
            imageAdapter.changeFav(it)

        }
    }

    override fun initData() {
        viewModel.getListImage()
    }

    override fun initObserver() {
        viewModel.listImage.onEach {
            imageAdapter.setData(ArrayList(it))
            Log.d("TAG", "initObserver: ${it.size}")
        }.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED).launchIn(lifecycleScope)
    }
}