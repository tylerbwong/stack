package me.tylerbwong.stack.ui.profile

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import me.tylerbwong.adapter.DynamicItem
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.User
import me.tylerbwong.stack.api.service.UserService
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.home.AnswerItem
import me.tylerbwong.stack.ui.home.QuestionItem
import me.tylerbwong.stack.ui.utils.toHtml
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val service: UserService
) : BaseViewModel() {

    internal var userId: Int? = null
    private var user: User? = null

    internal var currentPage = ProfilePage.QUESTIONS

    internal val userData: LiveData<User>
        get() = _userData
    private val _userData = MutableLiveData<User>()

    internal val data: LiveData<List<DynamicItem>>
        get() = _data
    private val _data = MutableLiveData<List<DynamicItem>>()

    internal fun fetchUserData() {
        launchRequest {
            user = service.getUser(userId).items.firstOrNull()?.also { _userData.value = it }
        }
    }

    internal fun fetchProfileData(page: ProfilePage = currentPage) {
        launchRequest {
            _data.value = when (page) {
                ProfilePage.QUESTIONS -> service.getQuestionsByUserId(userId).items.map { QuestionItem(it) }
                ProfilePage.ANSWERS -> service.getAnswersByUserId(userId).items.map { AnswerItem(it) }
//                ProfilePage.ACTIVITY -> service.getUserTimeline(userId).items.mapNotNull {
//                    when (it.timelineType) {
//                        "accepted" -> AcceptedItem(it)
//                        "answered" -> AnsweredItem(it)
//                        "asked" -> AskedItem(it)
//                        "badge" -> BadgeItem(it)
//                        "commented" -> CommentedItem(it)
//                        "reviewed" -> ReviewedItem(it)
//                        "revision" -> RevisionItem(it)
//                        "suggested" -> SuggestedItem(it)
//                        else -> null
//                    }
//                }
            }
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

    enum class ProfilePage {
        QUESTIONS,
        ANSWERS,
    }
}
