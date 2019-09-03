package me.tylerbwong.stack.ui.profile

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.User
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.network.service.UserService
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.answers.AnswerDataModel
import me.tylerbwong.stack.ui.questions.QuestionDataModel

class ProfileViewModel(
        private val service: UserService = ServiceProvider.userService
) : BaseViewModel() {

    internal var userId: Int? = null
    private var user: User? = null

    internal val userData: LiveData<User>
        get() = _userData
    private val _userData = MutableLiveData<User>()

    internal val questionsData: LiveData<List<QuestionDataModel>>
        get() = _questionsData
    private val _questionsData = MutableLiveData<List<QuestionDataModel>>()

    internal val answersData: LiveData<List<AnswerDataModel>>
        get() = _answersData
    private val _answersData = MutableLiveData<List<AnswerDataModel>>()

    internal fun getUserQuestionsAndAnswers() {
        launchRequest {
            _userData.value = service.getUser(userId).items.firstOrNull()
            _questionsData.value = service.getUserQuestionsById(userId).items.map {
                QuestionDataModel(it)
            }
            // TODO Show user answers
//            _answersData.value = service.getUserAnswersById(userId).items.map {
//                AnswerDataModel(it)
//            }
            user = questionsData.value?.firstOrNull()?.owner
        }
    }

    internal fun startShareIntent(context: Context) {
        user?.let {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = SHARE_TEXT_TYPE
                putExtra(Intent.EXTRA_SUBJECT, it.displayName)
                putExtra(Intent.EXTRA_TEXT, it.link)
            }
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)))
        }
    }
}
