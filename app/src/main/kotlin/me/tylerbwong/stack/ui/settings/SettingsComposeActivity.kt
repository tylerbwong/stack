package me.tylerbwong.stack.ui.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.stack.data.di.StackSharedPreferences
import me.tylerbwong.stack.ui.BaseActivity
import javax.inject.Inject

@AndroidEntryPoint
class SettingsComposeActivity : BaseActivity<ViewBinding>(bindingProvider = null) {

    @[Inject StackSharedPreferences]
    lateinit var stackSharedPreferences: SharedPreferences

    private val viewModel by viewModels<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SettingsScreen(
                preferences = stackSharedPreferences,
                onBackPressed = { onBackPressed() }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchData()
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, SettingsComposeActivity::class.java))
        }
    }
}
