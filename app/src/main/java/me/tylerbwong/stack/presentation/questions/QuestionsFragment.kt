package me.tylerbwong.stack.presentation.questions

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.questions_fragment.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.inflateWithoutAttaching
import me.tylerbwong.stack.presentation.BaseFragment
import me.tylerbwong.stack.presentation.ViewHolderItemDecoration

class QuestionsFragment : BaseFragment(), QuestionsContract.View {

    private val presenter: QuestionsContract.Presenter = QuestionsPresenter(this)
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
            this@apply.adapter = this@QuestionsFragment.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ViewHolderItemDecoration(
                    context.resources.getDimensionPixelSize(R.dimen.item_spacing)
            ))
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe()
    }

    override fun setQuestions(questions: List<Question>) {
        adapter.questions = questions
    }

    companion object {
        fun newInstance() = QuestionsFragment()
    }
}