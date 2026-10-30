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

        tvDark.setOnClickListener {
            selectTheme(Constant.THEME_DARK)
        }

        tvLight.setOnClickListener {
            selectTheme(Constant.THEME_LIGHT)
        }

        tvSystem.setOnClickListener {
            selectTheme(Constant.THEME_SYSTEM)
        }

        btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun updateSelection(themeMode: Int) = with(viewBinding) {
        val selectedColor = ContextCompat.getColor(context, R.color.purple2)
        val defaultColor = ContextCompat.getColor(context, R.color.white)

        tvDark.setTextColor(if (themeMode == Constant.THEME_DARK) selectedColor else defaultColor)
        tvLight.setTextColor(if (themeMode == Constant.THEME_LIGHT) selectedColor else defaultColor)
        tvSystem.setTextColor(if (themeMode == Constant.THEME_SYSTEM) selectedColor else defaultColor)
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