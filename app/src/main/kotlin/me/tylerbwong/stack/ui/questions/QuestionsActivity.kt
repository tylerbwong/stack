package me.tylerbwong.stack.ui.questions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.ui.platform.setContent
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.BaseActivity

@AndroidEntryPoint
class QuestionsActivity : BaseActivity<ViewBinding>(
    bindingProvider = null // TODO Remove when Hilt supports default constructor values
) {

    private val viewModel by viewModels<QuestionsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val key = intent.getStringExtra(KEY_EXTRA) ?: ""

        if (key.isBlank()) {
            Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG).show()
            finish()
        }

        val page = intent.getSerializableExtra(PAGE_EXTRA) as QuestionPage

        setUpPageForKey(page, key)

        setContent {
            QuestionsScreen(
                title = if (page.titleRes != null) {
                    getString(page.titleRes)
                } else {
                    key
                },
                onBackPressed = ::onBackPressed
            )
        }
    }

    private fun setUpPageForKey(page: QuestionPage, key: String) {
        viewModel.page = page
        viewModel.key = key

        viewModel.getQuestions()
    }

    companion object {
        private const val PAGE_EXTRA = "page"
        private const val KEY_EXTRA = "key"

        fun makeIntentForKey(
            context: Context,
            page: QuestionPage,
            key: String
        ) = Intent(context, QuestionsActivity::class.java)
            .putExtra(PAGE_EXTRA, page)
            .putExtra(KEY_EXTRA, key)

        fun startActivityForKey(context: Context, page: QuestionPage, key: String) {
            context.startActivity(makeIntentForKey(context, page, key))
        }
    }
}
