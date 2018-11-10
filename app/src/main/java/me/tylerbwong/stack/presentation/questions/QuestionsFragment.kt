package me.tylerbwong.stack.presentation.questions

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.questions_fragment.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.CREATION
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.model.Sort
import me.tylerbwong.stack.presentation.BaseFragment
import me.tylerbwong.stack.presentation.ViewHolderItemDecoration
import me.tylerbwong.stack.presentation.utils.inflateWithoutAttaching

class QuestionsFragment : BaseFragment() {

    private lateinit var viewModel: QuestionsViewModel
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
        viewModel = ViewModelProviders.of(this).get(QuestionsViewModel::class.java)
        viewModel.refreshing.observe(this, Observer {
            refreshLayout?.isRefreshing = it
        })
        viewModel.questions.observe(this, Observer {
            adapter.questions = it
        })

        recyclerView.apply {
            adapter = this@QuestionsFragment.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                    ViewHolderItemDecoration(
                            context.resources.getDimensionPixelSize(R.dimen.item_spacing)
                    )
            )
        }
        refreshLayout.setOnRefreshListener { sortQuestions() }

        sortQuestions()
    }

    fun sortQuestions(@Sort sort: String = CREATION) = viewModel.getQuestions(sort)

    fun searchQuestions(query: String) = viewModel.searchQuestions(query)

    companion object {
        fun newInstance() = QuestionsFragment()
    }
}
