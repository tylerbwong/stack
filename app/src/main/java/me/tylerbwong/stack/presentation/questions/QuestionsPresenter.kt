package me.tylerbwong.stack.presentation.questions

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import me.tylerbwong.stack.data.network.ServiceProvider
import timber.log.Timber

internal class QuestionsPresenter(
        private val view: QuestionsContract.View,
        private val disposables: CompositeDisposable = CompositeDisposable()
) : QuestionsContract.Presenter {

    override fun getQuestions(sort: String) {
        view.setRefreshing(true)
        val disposable = ServiceProvider.questionService.getQuestions(sort = sort)
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

    override fun searchQuestions(query: String) {
        view.setRefreshing(true)
        val disposable = ServiceProvider.questionService.getQuestionsBySearchString(searchString = query)
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
