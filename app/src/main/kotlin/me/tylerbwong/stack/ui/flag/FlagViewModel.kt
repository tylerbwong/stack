package me.tylerbwong.stack.ui.flag

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.api.model.FlagOption
import me.tylerbwong.stack.api.service.FlagService
import me.tylerbwong.stack.ui.BaseViewModel
import me.tylerbwong.stack.ui.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class FlagViewModel @Inject constructor(private val flagService: FlagService) : BaseViewModel() {

    internal var postType: FlagPostType? = null

    val flagOptions: LiveData<List<FlagOption>>
        get() = _flagOptions
    private val _flagOptions = MutableLiveData<List<FlagOption>>()

    val success: LiveData<Boolean?>
        get() = _success
    private val _success = SingleLiveEvent<Boolean?>()

    var extraComment by mutableStateOf<String?>(null)

    fun getFlagOptions() {
        postType?.let {
            launchRequest {
                _flagOptions.value = when (it) {
                    is FlagPostType.Question -> flagService.getQuestionFlagOptions(it.id)
                    is FlagPostType.Answer -> flagService.getAnswerFlagOptions(it.id)
                    is FlagPostType.Comment -> flagService.getCommentFlagOptions(it.id)
                }.items.filter { it.title != null }
            }
        }
    }

    fun addFlag(option: FlagOption?) {
        option?.optionId?.let { optionId ->
            postType?.let {
                launchRequest {
                    val response = when (it) {
                        is FlagPostType.Question -> flagService.addQuestionFlag(
                            id = it.id,
                            optionId = optionId,
                            comment = extraComment,
                            preview = BuildConfig.DEBUG,
                        )
                        is FlagPostType.Answer -> flagService.addAnswerFlag(
                            id = it.id,
                            optionId = optionId,
                            comment = extraComment,
                            preview = BuildConfig.DEBUG,
                        )
                        is FlagPostType.Comment -> flagService.addCommentFlag(
                            id = it.id,
                            optionId = optionId,
                            comment = extraComment,
                            preview = BuildConfig.DEBUG,
                        )
                    }
                    _success.value = response.items.isNotEmpty()
                }
            }
        }
    }

    internal fun getNextPageForOption(option: FlagOption?): FlagPage? {
        return when {
            option?.subOptions?.isNotEmpty() == true -> FlagPage.Options
            option?.requiresComment == true -> FlagPage.Comment
            option?.requiresQuestionId == true -> FlagPage.Duplicate
            else -> null // We are ready to submit
        }
    }

    sealed class FlagPostType(val id: Int) {
        class Question(id: Int) : FlagPostType(id)
        class Answer(id: Int) : FlagPostType(id)
        class Comment(id: Int) : FlagPostType(id)
    }
}
