package com.duongnd.wallpaper.presentation.dialog

import android.app.Activity
import android.content.Context
import androidx.core.content.ContextCompat
import com.duongnd.wallpaper.R
import com.duongnd.wallpaper.base.dialog.BaseDialog
import com.duongnd.wallpaper.databinding.DialogSetThemeBinding
import com.duongnd.wallpaper.utils.Constant
import com.duongnd.wallpaper.utils.FirebaseAnalyticsUtils
import com.duongnd.wallpaper.utils.SpManager
import com.duongnd.wallpaper.utils.applyThemeMode

class ThemeDialog(
    private val context: Context,
    private val onThemeChanged: ((Int) -> Unit)? = null
) : BaseDialog<DialogSetThemeBinding>(context) {

    private val spManager by lazy { SpManager.getInstance(context) }

    override fun provideViewBinding(): DialogSetThemeBinding {
        return DialogSetThemeBinding.inflate(layoutInflater)
    }

    override fun initViews() = with(viewBinding) {
        super.initViews()
        setCancelable(true)

        updateSelection(spManager.getThemeMode())

        layoutDark.setOnClickListener {
            selectTheme(Constant.THEME_DARK)
        }

        layoutLight.setOnClickListener {
            selectTheme(Constant.THEME_LIGHT)
        }

        layoutSystem.setOnClickListener {
            selectTheme(Constant.THEME_SYSTEM)
        }

        btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun updateSelection(themeMode: Int) = with(viewBinding) {
        val isDark = themeMode == Constant.THEME_DARK
        val isLight = themeMode == Constant.THEME_LIGHT
        val isSystem = themeMode == Constant.THEME_SYSTEM

        imgDarkCheck.setImageResource(if (isDark) R.drawable.ic_checkbox_checked else R.drawable.ic_checkbox_normal)
        imgLightCheck.setImageResource(if (isLight) R.drawable.ic_checkbox_checked else R.drawable.ic_checkbox_normal)
        imgSystemCheck.setImageResource(if (isSystem) R.drawable.ic_checkbox_checked else R.drawable.ic_checkbox_normal)

        val selectedColor = ContextCompat.getColor(context, R.color.purple2)
        val defaultColor = ContextCompat.getColor(context, R.color.text_main)

        tvDark.setTextColor(if (isDark) selectedColor else defaultColor)
        tvLight.setTextColor(if (isLight) selectedColor else defaultColor)
        tvSystem.setTextColor(if (isSystem) selectedColor else defaultColor)
    }

    private fun selectTheme(themeMode: Int) {
        spManager.saveThemeMode(themeMode)
        context.applyThemeMode(themeMode)
        onThemeChanged?.invoke(themeMode)

        val themeStr = when (themeMode) {
            Constant.THEME_DARK -> "dark"
            Constant.THEME_LIGHT -> "light"
            Constant.THEME_SYSTEM -> "system"
            else -> "system"
        }
        FirebaseAnalyticsUtils.logChangeTheme(themeStr)
        dismiss()
        (context as? Activity)?.recreate()
    }
}
