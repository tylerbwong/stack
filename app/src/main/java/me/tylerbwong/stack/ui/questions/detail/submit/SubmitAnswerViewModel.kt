package me.tylerbwong.stack.ui.questions.detail.submit

import android.text.TextWatcher
import androidx.lifecycle.ViewModel

class SubmitAnswerViewModel : ViewModel() {
    internal var markdownTextWatcher: TextWatcher? = null
    internal var selectedTabPosition = 0
}
