package com.duongnd.wallpaper.dialog

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import androidx.core.net.toUri
import com.duongnd.wallpaper.base.dialog.BaseDialog
import com.duongnd.wallpaper.databinding.DialogForceUpdateBinding

class ForceUpdateDialog(private val context: Context) :
    BaseDialog<DialogForceUpdateBinding>(context) {

    var onExit: () -> Unit = {}

    override fun provideViewBinding(): DialogForceUpdateBinding {
        return DialogForceUpdateBinding.inflate(layoutInflater)
    }

    override fun initViews() = with(viewBinding) {
        super.initViews()
        setCancelable(false)
        setCanceledOnTouchOutside(false)

        // Block back button dismiss
        setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                onExit.invoke()
                (context as? Activity)?.finishAffinity()
                true
            } else {
                false
            }
        }

        btnUpdate.setOnClickListener {
            openPlayStore()
        }

        btnExit.setOnClickListener {
            onExit.invoke()
            (context as? Activity)?.finishAffinity()
        }
    }

    private fun openPlayStore() {
        val packageName = context.packageName
        try {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri())
            )
        } catch (_: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=$packageName".toUri()
                )
            )
        }
    }
}
