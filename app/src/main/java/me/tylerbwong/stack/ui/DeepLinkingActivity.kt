package me.tylerbwong.stack.ui

import android.os.Bundle
import android.widget.Toast
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.DeepLinker
import javax.inject.Inject

class DeepLinkingActivity : BaseActivity() {

    @Inject
    lateinit var deepLinker: DeepLinker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationWrapper.stackComponent.inject(this)
        intent.data?.let {
            val resolvedIntent = deepLinker.resolvePath(this, it)

            if (resolvedIntent != null) {
                startActivity(resolvedIntent)
            } else {
                Toast.makeText(this, R.string.deep_link_error, Toast.LENGTH_LONG).show()
            }
        }
        finish()
    }
}
