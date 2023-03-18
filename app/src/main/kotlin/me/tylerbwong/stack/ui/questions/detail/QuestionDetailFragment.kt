package me.tylerbwong.stack.ui.questions.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.updatePadding
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import me.tylerbwong.adapter.DynamicListAdapter
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.QuestionDetailFragmentBinding
import me.tylerbwong.stack.ui.BaseFragment
import me.tylerbwong.stack.ui.flag.FlagActivity
import me.tylerbwong.stack.ui.questions.QuestionPage.LINKED
import me.tylerbwong.stack.ui.questions.QuestionPage.RELATED
import me.tylerbwong.stack.ui.questions.QuestionsActivity
import me.tylerbwong.stack.ui.utils.ViewHolderItemDecoration
import me.tylerbwong.stack.ui.utils.copyToClipboard
import me.tylerbwong.stack.ui.utils.hideKeyboard
import me.tylerbwong.stack.ui.utils.launchUrl
import me.tylerbwong.stack.ui.utils.ofType
import me.tylerbwong.stack.ui.utils.showDialog
import me.tylerbwong.stack.ui.utils.showRegisterOnSiteDialog
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

    @Suppress("LongMethod")
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
                if (viewModel.isAuthenticated && viewModel.user.value == null) {
                    setAction(R.string.register) { showRegisterOnSiteDialog() }
                } else {
                    setAction(R.string.dismiss) { dismiss() }
                }
                show()
            }
        }
        viewModel.showRegisterDialog.observe(viewLifecycleOwner) {
            showRegisterOnSiteDialog()
        }
        viewModel.showCopyDialog.observe(viewLifecycleOwner) {
            showCopyDialog(it)
        }
        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.scrollToIndex.observe(viewLifecycleOwner) { index ->
            if (index != null && index != -1) {
                binding.recyclerView.scrollToPosition(index)
            }
        }
        viewModel.voteCount.observe(viewLifecycleOwner) {
            activity?.ofType<QuestionDetailActivity>()?.setTitle(
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
            addOnScrollListener(
                QuestionDetailOnScrollListener { dy ->
                    val activity = activity?.ofType<QuestionDetailActivity>()
                    if (dy > 0) {
                        activity?.shrinkAnswerButton()
                    } else if (dy < 0) {
                        activity?.extendAnswerButton()
                    }
                }
            )

            if (viewModel.isAuthenticated) {
                updatePadding(
                    bottom = resources.getDimensionPixelOffset(
                        R.dimen.question_detail_recycler_view_padding
                    )
                )
            }
            applyInsetter {
                type(ime = true, statusBars = true, navigationBars = true) {
                    padding(bottom = true)
                }
            }
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
            R.id.open_browser -> viewModel.question?.shareLink?.let {
                requireContext().launchUrl(it, forceExternal = true)
            }
            R.id.flag -> {
                val intent = FlagActivity.makeIntent(
                    context = requireContext(),
                    postId = viewModel.questionId,
                    postType = 0,
                )
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showRegisterOnSiteDialog() {
        viewModel.site.value?.let { site ->
            requireContext().showRegisterOnSiteDialog(
                site = site,
                siteUrl = viewModel.buildSiteJoinUrl(site),
                titleResId = R.string.register_on_site_contribute,
            )
        }
    }

    private fun showCopyDialog(copyData: QuestionDetailMainViewModel.CopyData) {
        requireContext().showDialog {
            setIcon(R.drawable.ic_baseline_content_copy)
            setTitle(
                if (copyData.titleText != null) {
                    R.string.copy_question_content
                } else {
                    R.string.copy_answer_content
                }
            )
            val titleText = getString(R.string.copy_title)
            val bodyText = getString(R.string.copy_body)
            val bodyMarkdownText = getString(R.string.copy_body_markdown)
            val linkText = getString(R.string.copy_link)
            val choices = if (copyData.titleText != null) {
                listOf(titleText)
            } else {
                emptyList()
            } + listOf(bodyText, bodyMarkdownText, linkText)
            setItems(choices.toTypedArray()) { _, which ->
                val content = when (choices[which]) {
                    titleText -> titleText to (copyData.titleText ?: "")
                    bodyText -> bodyText to copyData.bodyText
                    bodyMarkdownText -> bodyMarkdownText to copyData.bodyMarkdown
                    linkText -> linkText to copyData.linkText
                    else -> null
                }
                if (content != null) {
                    requireContext().copyToClipboard(label = content.first, text = content.second)
                }
            }
        }
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
