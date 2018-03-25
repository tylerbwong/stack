package me.tylerbwong.stack.presentation.questions.detail

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import me.tylerbwong.stack.data.network.ServiceProvider
import timber.log.Timber

class QuestionDetailPresenter(
        private val view: QuestionDetailContract.View
) : QuestionDetailContract.Presenter {

    internal var questionId: Int = 0

    private val disposables = CompositeDisposable()

    private fun getQuestion() {
        val disposable = ServiceProvider.questionService.getQuestionDetails(questionId)
                .map { it.items.first() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::setQuestion, Timber::e)
        disposables.add(disposable)
    }

    private fun getAnswers() {
        view.setRefreshing(true)
        val disposable = ServiceProvider.questionService.getQuestionAnswers(questionId)
                .map { it.items }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.setAnswers(it)
                    view.setRefreshing(false)
                }, {
                    Timber.e(it)
                    view.setRefreshing(false)
                })
        disposables.add(disposable)
    }

    override fun subscribe() {
        getQuestion()
        getAnswers()
    }

    override fun unsubscribe() = disposables.clear()
}
