package me.tylerbwong.stack.presentation.questions

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.tylerbwong.stack.R
import me.tylerbwong.stack.inflateWithoutAttaching
import me.tylerbwong.stack.presentation.BaseView

class QuestionsFragment : Fragment(), BaseView<QuestionsPresenter> {

    private lateinit var presenter: QuestionsPresenter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return container?.inflateWithoutAttaching(R.layout.fragment_questions)
    }

    override fun onResume() {
        super.onResume()

    }

    override fun setPresenter(presenter: QuestionsPresenter) {
        this.presenter = presenter
    }

    companion object {
        fun newInstance() = QuestionsFragment()
    }
}