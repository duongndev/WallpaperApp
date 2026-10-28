package com.duongnd.wallpaper.component.setting

import android.content.Context
import android.content.Intent
import android.view.View
import com.duongnd.wallpaper.BuildConfig
import com.duongnd.wallpaper.R
import com.duongnd.wallpaper.base.activity.BaseActivity
import com.duongnd.wallpaper.databinding.ActivitySettingBinding
import com.duongnd.wallpaper.dialog.RateDialog
import com.duongnd.wallpaper.utils.Constant
import com.duongnd.wallpaper.utils.gone
import com.duongnd.wallpaper.utils.openBrowser
import com.duongnd.wallpaper.utils.share
import com.duongnd.wallpaper.utils.visible

class SettingActivity : BaseActivity<ActivitySettingBinding>(), View.OnClickListener {
    override fun provideViewBinding(): ActivitySettingBinding =
        ActivitySettingBinding.inflate(layoutInflater)

    override fun initViews() = with(viewBinding) {
        super.initViews()
        toolBar.btnBack.visible()
        toolBar.btnFav.gone()
        toolBar.btnSetting.gone()
        toolBar.tvTitle.text = getString(R.string.settings)
        toolBar.btnBack.setOnClickListener {
            finish()
        }
        btnLanguage.setOnClickListener(this@SettingActivity)
        btnShareApp.setOnClickListener(this@SettingActivity)
        btnRateUs.setOnClickListener(this@SettingActivity)
        btnPrivacyPolicy.setOnClickListener(this@SettingActivity)
        btnPolicySetting.setOnClickListener(this@SettingActivity)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.btnLanguage -> {

            }

            R.id.btnShareApp -> {
                share("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
            }

            R.id.btnRateUs -> {
                RateDialog(this).show()
            }

            R.id.btnPrivacyPolicy -> {
                openBrowser(Constant.LINK_POLICY)
            }

            R.id.btnPolicySetting -> {
                showPolicySetting()
            }
        }
    }

    private fun showPolicySetting() {
//        UserMessagingPlatform.showPrivacyOptionsForm(this) { formError ->
//            Logger.e("${formError?.errorCode} -- ${formError?.message}")
//        }
    }

    companion object {
        fun start(context: Context) {
            Intent(context, SettingActivity::class.java).also {
                context.startActivity(it)
            }
        }
    }
}