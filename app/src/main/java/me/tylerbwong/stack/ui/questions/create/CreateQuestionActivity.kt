package me.tylerbwong.stack.ui.questions.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.ui.core.setContent
import androidx.viewbinding.ViewBinding
import me.tylerbwong.stack.ui.BaseActivity

class CreateQuestionActivity : BaseActivity<ViewBinding>(
    bindingProvider = null // TODO Remove when Hilt supports default constructor values
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreateQuestionLayout {
                super.onBackPressed()
            }
        }
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, CreateQuestionActivity::class.java))
        }
    }
}
