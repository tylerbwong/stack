package me.tylerbwong.stack.ui

import android.os.Bundle
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.DeepLinkResult
import me.tylerbwong.stack.data.DeepLinker
import javax.inject.Inject

@AndroidEntryPoint
class DeepLinkingActivity : BaseActivity<ViewBinding>(
    bindingProvider = null // TODO Remove when Hilt supports default constructor values
) {

    @Inject
    lateinit var deepLinker: DeepLinker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.data?.let {
            when (val result = deepLinker.resolvePath(this, it)) {
                is DeepLinkResult.Success -> startActivity(result.intent)
                is DeepLinkResult.PathNotSupportedError ->
                    Toast.makeText(this, R.string.deep_link_path_not_supported, Toast.LENGTH_LONG).show()
            }
        }
        finish()
    }
}
