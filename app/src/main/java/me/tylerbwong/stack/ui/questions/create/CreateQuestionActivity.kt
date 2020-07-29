package me.tylerbwong.stack.ui.questions.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.ActivityCreateQuestionBinding
import me.tylerbwong.stack.ui.BaseActivity

@AndroidEntryPoint
class CreateQuestionActivity : BaseActivity<ActivityCreateQuestionBinding>(
    ActivityCreateQuestionBinding::inflate
) {

    private val createQuestionFragment by lazy {
        initializeFragment(FRAGMENT_TAG, ::CreateQuestionFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .show(createQuestionFragment)
                .commit()
        }
    }

    private fun initializeFragment(tag: String, createFragment: () -> Fragment): Fragment {
        return supportFragmentManager.findFragmentByTag(tag) ?: createFragment().also { fragment ->
            supportFragmentManager
                .beginTransaction()
                .add(R.id.rootLayout, fragment, tag)
                .hide(fragment)
                .commit()
        }
    }

    companion object {

        private const val FRAGMENT_TAG = "create_question_fragment"

        fun startActivity(context: Context) {
            context.startActivity(Intent(context, CreateQuestionActivity::class.java))
        }
    }
}
