package com.duongnd.wallpaper.presentation.dialog

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.duongnd.wallpaper.base.dialog.BaseDialog
import com.duongnd.wallpaper.databinding.DialogExitAppBinding

class ExitAppDialog(private val context: Context) :
    BaseDialog<DialogExitAppBinding>(context) {

    var onExit: () -> Unit = {}

    override fun provideViewBinding(): DialogExitAppBinding {
        return DialogExitAppBinding.inflate(layoutInflater)
    }

    override fun initViews() = with(viewBinding) {
        super.initViews()
        setCancelable(false)

        btnKeepUsingApp.setOnClickListener {
            dismiss()
        }

        btnExitApp.setOnClickListener {
            dismiss()
            onExit.invoke()
        }
    }

}
