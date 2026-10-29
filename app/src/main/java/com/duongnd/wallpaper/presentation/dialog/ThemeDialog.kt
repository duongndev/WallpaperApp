package com.duongnd.wallpaper.presentation.dialog

import android.content.Context
import com.duongnd.wallpaper.base.dialog.BaseDialog
import com.duongnd.wallpaper.databinding.DialogSetThemeBinding


class ThemeDialog(private val context: Context): BaseDialog<DialogSetThemeBinding>(context)  {
    override fun provideViewBinding(): DialogSetThemeBinding {
        return DialogSetThemeBinding.inflate(layoutInflater)
    }

    override fun initViews() = with(viewBinding) {
        super.initViews()
        setCancelable(true)

        btnCancel.setOnClickListener {
            dismiss()
        }
    }
}