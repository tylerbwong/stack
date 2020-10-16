package me.tylerbwong.stack.ui.profile

import android.content.Context
import android.content.Intent
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.User
import me.tylerbwong.stack.api.service.UserService
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.home.QuestionItem
import me.tylerbwong.stack.ui.utils.toHtml

class ProfileViewModel @ViewModelInject constructor(
    private val service: UserService
) : BaseViewModel() {

    internal var userId: Int? = null
    private var user: User? = null

    internal val userData: LiveData<User>
        get() = _userData
    private val _userData = MutableLiveData<User>()

    internal val questionsData: LiveData<List<QuestionItem>>
        get() = _questionsData
    private val _questionsData = MutableLiveData<List<QuestionItem>>()

    internal fun getUserQuestionsAndAnswers() {
        launchRequest {
            user = service.getUser(userId).items.firstOrNull()?.also { _userData.value = it }
            _questionsData.value = service.getUserQuestionsById(userId).items.map { QuestionItem(it) }
        }
    }

    internal fun startShareIntent(context: Context) {
        user?.let {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = SHARE_TEXT_TYPE
                putExtra(Intent.EXTRA_SUBJECT, it.displayName.toHtml().toString())
                putExtra(Intent.EXTRA_TEXT, it.link)
            }
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)))
        }
    }
}
