package com.duongnd.wallpaper.presentation.dialog

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.duongnd.wallpaper.base.dialog.BaseDialog
import com.duongnd.wallpaper.databinding.DialogSetThemeBinding
import com.duongnd.wallpaper.utils.FirebaseAnalyticsUtils


class ThemeDialog(private val context: Context): BaseDialog<DialogSetThemeBinding>(context)  {
    override fun provideViewBinding(): DialogSetThemeBinding {
        return DialogSetThemeBinding.inflate(layoutInflater)
    }

    override fun initViews() = with(viewBinding) {
        super.initViews()
        setCancelable(true)

        tvDark.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            FirebaseAnalyticsUtils.logChangeTheme("dark")
            dismiss()
        }

        tvLight.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            FirebaseAnalyticsUtils.logChangeTheme("light")
            dismiss()
        }

        tvSystem.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            FirebaseAnalyticsUtils.logChangeTheme("system")
            dismiss()
        }

        btnCancel.setOnClickListener {
            dismiss()
        }
    }
}