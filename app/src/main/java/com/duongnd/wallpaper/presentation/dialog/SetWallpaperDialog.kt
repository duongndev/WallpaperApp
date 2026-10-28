package com.duongnd.wallpaper.presentation.dialog

import android.content.Context
import com.duongnd.wallpaper.base.dialog.BaseDialog
import com.duongnd.wallpaper.databinding.DialogSetWallpaperBinding

class SetWallpaperDialog(
    context: Context,
    private val onHomeScreenClick: () -> Unit,
    private val onLockScreenClick: () -> Unit,
    private val onBothScreenClick: () -> Unit
) : BaseDialog<DialogSetWallpaperBinding>(context) {


    override fun provideViewBinding(): DialogSetWallpaperBinding = DialogSetWallpaperBinding.inflate(layoutInflater)


    override fun initViews() = with(viewBinding) {
        super.initViews()
        tvHomeScreen.setOnClickListener {
            onHomeScreenClick()
            dismiss()
        }

        tvLockScreen.setOnClickListener {
            onLockScreenClick()
            dismiss()
        }

        tvBothScreen.setOnClickListener {
            onBothScreenClick()
            dismiss()
        }

        btnCancel.setOnClickListener {
            dismiss()
        }
    }

}
