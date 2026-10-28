package com.duongnd.wallpaper.component.language.activity

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.duongnd.wallpaper.R
import com.duongnd.wallpaper.base.activity.BaseActivity
import com.duongnd.wallpaper.component.language.adapter.LanguageAdapter
import com.duongnd.wallpaper.component.language.viewmodel.LanguageViewModel
import com.duongnd.wallpaper.component.main.activity.MainActivity
import com.duongnd.wallpaper.databinding.ActivityLanguageBinding
import com.duongnd.wallpaper.domain.model.Language
import com.duongnd.wallpaper.utils.SpManager
import com.duongnd.wallpaper.utils.gone
import com.duongnd.wallpaper.utils.setAppLanguage
import com.duongnd.wallpaper.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class LanguageActivity : BaseActivity<ActivityLanguageBinding>() {
    override fun provideViewBinding(): ActivityLanguageBinding =
        ActivityLanguageBinding.inflate(layoutInflater)

    private var selectLanguageModel: Language? = null
    private val viewModel: LanguageViewModel by viewModels()
    private val languageAdapter = LanguageAdapter()

    @Inject
    lateinit var spManager: SpManager

    override fun initViews() = with(viewBinding) {
        super.initViews()
        toolBar.btnBack.visible()
        toolBar.btnSetting.gone()
        toolBar.btnSetting.setImageResource(R.drawable.ic_check)
        toolBar.btnFav.gone()
        toolBar.tvTitle.text = resources.getString(R.string.language)
        toolBar.btnBack.setOnClickListener {
            finish()
        }
        toolBar.btnSetting.setOnClickListener {
            changeLanguage()
        }
        rcvLanguage.adapter = languageAdapter

        languageAdapter.onClick = { item ->
            languageAdapter.selectLanguage(item.languageCode)
            languageAdapter.selectedLanguage()?.let { selected ->
                selectLanguageModel = selected
                toolBar.btnSetting.visible()
            }
        }
    }

    private fun changeLanguage() {
        val selected = selectLanguageModel ?: return
        spManager.saveLanguage(selected)
        spManager.setLanguageChosen()
        setAppLanguage(selected.languageCode)

        Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }.also {
            startActivity(it)
        }
        finish()
    }

    override fun initData() {
        viewModel.loadListLanguage()
    }

    override fun initObserver() {
        viewModel.listLanguage.onEach {
            languageAdapter.setData(ArrayList(it))
            val currentLanguageCode = spManager.getLanguage().languageCode
            languageAdapter.selectLanguage(currentLanguageCode)
            selectLanguageModel = languageAdapter.selectedLanguage()
        }.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED).launchIn(lifecycleScope)
    }

    companion object {
        fun start(context: Context) {
            Intent(context, LanguageActivity::class.java).also {
                context.startActivity(it)
            }
        }
    }
}