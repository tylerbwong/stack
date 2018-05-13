package me.tylerbwong.stack.presentation.questions

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import me.tylerbwong.stack.data.model.Sort
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.persistence.StackDatabase
import me.tylerbwong.stack.data.repository.QuestionRepository
import timber.log.Timber

internal class QuestionsPresenter(
        private val view: QuestionsContract.View,
        private val repository: QuestionRepository = QuestionRepository(StackDatabase.getInstance())
) : QuestionsContract.Presenter {

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun getQuestions(@Sort sort: String) {
        view.setRefreshing(true)
        val disposable = repository.getQuestions(sort)
                .observeOn(AndroidSchedulers.mainThread(), true)
                .doOnTerminate {
                    view.setRefreshing(false)
                }
                .subscribe({
                    view.setQuestions(it)
                }, {
                    Timber.e(it)
                    view.setRefreshing(false)
                })
        disposables.add(disposable)
    }

    override fun searchQuestions(query: String) {
        view.setRefreshing(true)
        val disposable = ServiceProvider.questionService
                .getQuestionsBySearchString(searchString = query)
                .map { it.items }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.setQuestions(it)
                    view.setRefreshing(false)
                }, {
                    Timber.e(it)
                    view.setRefreshing(false)
                })
        disposables.add(disposable)
    }

    override fun subscribe() {
        getQuestions()
    }

    override fun unsubscribe() = disposables.clear()
}
