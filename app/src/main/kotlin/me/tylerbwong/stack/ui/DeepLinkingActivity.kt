package me.tylerbwong.stack.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.DeepLinkResult
import me.tylerbwong.stack.data.auth.LoginResult

@AndroidEntryPoint
class DeepLinkingActivity : BaseActivity<ViewBinding>(
    bindingProvider = null // TODO Remove when Hilt supports default constructor values
) {
    private val viewModel by viewModels<DeepLinkingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loginResult.observe(this) { result ->
            Toast.makeText(
                this,
                when (result) {
                    is LoginResult.LoginSuccess -> R.string.log_in_success
                    is LoginResult.LoginError -> R.string.deep_link_user_not_found
                },
                Toast.LENGTH_LONG,
            ).show()
            finish()
        }
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.data?.let {
            when (val result = viewModel.resolvePath(this, it)) {
                is DeepLinkResult.Success -> {
                    startActivity(result.intent)
                    finish()
                }
                is DeepLinkResult.RequestingAuth -> viewModel.logIn(it)
                is DeepLinkResult.PathNotSupportedError -> {
                    Toast.makeText(
                        this,
                        R.string.deep_link_path_not_supported,
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            }
        }
    }
}
