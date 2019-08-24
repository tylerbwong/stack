package me.tylerbwong.stack.ui

import android.os.Bundle
import android.widget.Toast
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.DeepLinker

class DeepLinkingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.data?.let {
            val resolvedIntent = DeepLinker.resolvePath(this, it)

            if (resolvedIntent != null) {
                startActivity(resolvedIntent)
            } else {
                Toast.makeText(this, getString(R.string.deep_link_error), Toast.LENGTH_LONG).show()
            }
        }
        finish()
    }
}
