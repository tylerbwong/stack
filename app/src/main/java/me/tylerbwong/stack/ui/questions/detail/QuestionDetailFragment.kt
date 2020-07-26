package me.tylerbwong.stack.ui.questions.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.adapter.DynamicListAdapter
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.QuestionDetailFragmentBinding
import me.tylerbwong.stack.ui.BaseFragment
import me.tylerbwong.stack.ui.comments.CommentsBottomSheetDialogFragment
import me.tylerbwong.stack.ui.questions.QuestionPage.LINKED
import me.tylerbwong.stack.ui.questions.QuestionPage.RELATED
import me.tylerbwong.stack.ui.questions.QuestionsActivity
import me.tylerbwong.stack.ui.utils.ViewHolderItemDecoration
import me.tylerbwong.stack.ui.utils.hideKeyboard
import me.tylerbwong.stack.ui.utils.showSnackbar

@AndroidEntryPoint
class QuestionDetailFragment : BaseFragment<QuestionDetailFragmentBinding>(
    QuestionDetailFragmentBinding::inflate
) {
    private val viewModel by activityViewModels<QuestionDetailMainViewModel>()
    private val adapter = DynamicListAdapter(QuestionDetailItemCallback)
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.refreshing.observe(viewLifecycleOwner) {
            binding.refreshLayout.isRefreshing = it
        }
        viewModel.snackbar.observe(viewLifecycleOwner) {
            if (it != null) {
                snackbar = binding.refreshLayout.showSnackbar(
                    R.string.network_error,
                    R.string.retry
                ) { viewModel.getQuestionDetails() }
            } else {
                snackbar?.dismiss()
            }
        }
        viewModel.messageSnackbar.observe(viewLifecycleOwner) {
            with(Snackbar.make(binding.refreshLayout, it, Snackbar.LENGTH_INDEFINITE)) {
                this@with.view.findViewById<TextView>(
                    com.google.android.material.R.id.snackbar_text
                )?.apply {
                    maxLines = 4
                }
                setAction(R.string.dismiss) { dismiss() }
                show()
            }
        }
        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.voteCount.observe(viewLifecycleOwner) {
            (activity as? QuestionDetailActivity)?.setTitle(
                resources.getQuantityString(R.plurals.votes, it, it)
            )
        }

        binding.recyclerView.apply {
            adapter = this@QuestionDetailFragment.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                ViewHolderItemDecoration(
                    context.resources.getDimensionPixelSize(R.dimen.item_spacing_question_detail),
                    removeSideSpacing = true,
                    removeTopSpacing = true
                )
            )
            addOnScrollListener(object : OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val activity = activity as? QuestionDetailActivity
                    if (dy > 0) {
                        activity?.shrinkAnswerButton()
                    } else if (dy < 0) {
                        activity?.extendAnswerButton()
                    }
                }
            })
        }

        viewModel.questionId = arguments?.getInt(QuestionDetailActivity.QUESTION_ID, 0) ?: 0

        binding.refreshLayout.setOnRefreshListener { viewModel.getQuestionDetails() }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getQuestionDetails()
        binding.refreshLayout.hideKeyboard()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_share, menu)
        inflater.inflate(R.menu.menu_question_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> viewModel.startShareIntent(requireContext())
            R.id.comments -> CommentsBottomSheetDialogFragment.show(
                childFragmentManager,
                viewModel.questionId
            )
            R.id.linked -> QuestionsActivity.startActivityForKey(
                requireContext(),
                LINKED,
                viewModel.questionId.toString()
            )
            R.id.related -> QuestionsActivity.startActivityForKey(
                requireContext(),
                RELATED,
                viewModel.questionId.toString()
            )
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun newInstance(id: Int): QuestionDetailFragment {
            return QuestionDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(QuestionDetailActivity.QUESTION_ID, id)
                }
            }
        }
    }
}
