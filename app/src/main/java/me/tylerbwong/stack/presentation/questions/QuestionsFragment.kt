package me.tylerbwong.stack.presentation.questions

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.questions_fragment.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.model.Sort
import me.tylerbwong.stack.data.persistence.StackDatabase
import me.tylerbwong.stack.data.repository.QuestionRepository
import me.tylerbwong.stack.presentation.BaseFragment
import me.tylerbwong.stack.presentation.ViewHolderItemDecoration
import me.tylerbwong.stack.presentation.utils.inflateWithoutAttaching

class QuestionsFragment : BaseFragment(), QuestionsContract.View {

    private lateinit var presenter: QuestionsContract.Presenter
    private val adapter = QuestionsAdapter()

    override var titleRes: Int = R.string.questions

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return container?.inflateWithoutAttaching(R.layout.questions_fragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            adapter = this@QuestionsFragment.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                    ViewHolderItemDecoration(
                            context.resources.getDimensionPixelSize(R.dimen.item_spacing)
                    )
            )
        }
        refreshLayout.setOnRefreshListener { presenter.subscribe() }

        presenter.subscribe()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        presenter = QuestionsPresenter(this, QuestionRepository(StackDatabase.getInstance(context)))
    }

    override fun onStop() {
        super.onStop()
        presenter.unsubscribe()
    }

    override fun setQuestions(questions: List<Question>) {
        adapter.questions = questions
    }

    override fun setRefreshing(isRefreshing: Boolean) {
        refreshLayout?.isRefreshing = isRefreshing
    }

    fun sortQuestions(@Sort sort: String) = presenter.getQuestions(sort)

    fun searchQuestions(query: String) = presenter.searchQuestions(query)

    companion object {
        fun newInstance() = QuestionsFragment()
    }
}
