package com.duongnd.wallpaper.base.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.duongnd.wallpaper.R

abstract class BaseDialog<V : ViewBinding>(context: Context, theme: Int = R.style.Theme_Dialog) :
    Dialog(context, theme) {
    lateinit var viewBinding: V

    init {
        window?.setBackgroundDrawableResource(R.color.transparent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = provideViewBinding()
        setContentView(viewBinding.root)
        initViews()
        initData()
    }

    override fun show() {
        if (context is Activity && ((context as Activity).isFinishing || (context as Activity).isDestroyed)) {
            return
        }
        try {
            super.show()
        } catch (e: WindowManager.BadTokenException) {
            Log.e("BaseDialog", "Activity is not running or invalid window token", e)
        } catch (e: Exception) {
            Log.e("BaseDialog", "Error showing dialog", e)
        }
    }

    abstract fun provideViewBinding(): V
    open fun initViews() {}
    open fun initData() {}
}