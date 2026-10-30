package com.duongnd.wallpaper.presentation.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import com.duongnd.wallpaper.utils.Utils
import com.duongnd.wallpaper.R

class LoadingDialog : Dialog {
    private var mContext: Context

    private var dialog: LoadingDialog? = null

    constructor(context: Context) : super(context) {
        mContext = context
        dialog = LoadingDialog(mContext, R.style.CustomProgressLoading)
        dialog?.setTitle("")
        dialog?.setContentView(R.layout.dialog_loading)
        dialog?.setCancelable(false)
        dialog?.window?.attributes?.gravity = Gravity.CENTER
        val lp = dialog?.window?.attributes
        lp?.dimAmount = 0.2f
        dialog?.window?.attributes = lp
    }

    override fun show() {
        if (mContext is Activity && ((mContext as Activity).isFinishing || (mContext as Activity).isDestroyed)) {
            return
        }
        try {
            this.window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            )
            super.show()
            window?.let { Utils.fullScreenImmersive(it) }
            this.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        } catch (e: WindowManager.BadTokenException) {
            Log.e("LoadingDialog", "Activity is not running or invalid window token", e)
        } catch (e: Exception) {
            Log.e("LoadingDialog", "Error showing loading dialog", e)
        }
    }

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {
        mContext = context
    }

    fun showProgressDialog() {
        if (mContext is Activity && ((mContext as Activity).isFinishing || (mContext as Activity).isDestroyed)) {
            return
        }
        if (dialog != null && !dialog!!.isShowing) {
            dialog?.show()
        }
    }

    fun hideProgressDialog() {
        try {
            if (dialog != null && dialog!!.isShowing) {
                dialog?.dismiss()
            }
        } catch (e: Exception) {
            Log.e("LoadingDialog", "Error dismissing loading dialog", e)
        }
    }

}