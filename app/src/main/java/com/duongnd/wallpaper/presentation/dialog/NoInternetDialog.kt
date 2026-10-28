package com.duongnd.wallpaper.presentation.dialog

import android.content.Context
import com.duongnd.wallpaper.base.dialog.BaseDialog
import com.duongnd.wallpaper.databinding.DialogNoInternetBinding

class NoInternetDialog(private val context: Context) :
    BaseDialog<DialogNoInternetBinding>(context) {

    var onRetry: () -> Unit = {}
    var onCancel: () -> Unit = {}

    override fun provideViewBinding(): DialogNoInternetBinding {
        return DialogNoInternetBinding.inflate(layoutInflater)
    }

    override fun initViews() = with(viewBinding) {
        super.initViews()
        setCancelable(false)

        btnRetry.setOnClickListener {
            dismiss()
            onRetry.invoke()
        }

        btnCancel.setOnClickListener {
            dismiss()
            onCancel.invoke()
        }
    }

}
