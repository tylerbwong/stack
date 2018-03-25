package me.tylerbwong.stack.presentation.questions

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import me.tylerbwong.stack.data.network.ServiceProvider
import timber.log.Timber

internal class QuestionsPresenter(
        private val view: QuestionsContract.View,
        private val disposables: CompositeDisposable = CompositeDisposable()
) : QuestionsContract.Presenter {

    override fun subscribe() {
        view.setRefreshing(true)
        val disposable = ServiceProvider.questionService.getQuestions()
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

    override fun unsubscribe() = disposables.clear()
}
