package me.tylerbwong.stack.ui.comments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.adapter.DynamicListAdapter
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.CommentsFragmentBinding

@AndroidEntryPoint
class CommentsBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private val viewModel by viewModels<CommentsViewModel>()
    private val adapter = DynamicListAdapter(CommentItemCallback)
    private lateinit var binding: CommentsFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.postId = arguments?.getInt(POST_ID) ?: -1
        viewModel.commentId = arguments?.getInt(COMMENT_ID) ?: -1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CommentsFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.apply {
            adapter = this@CommentsBottomSheetDialogFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.header.title.text = getString(R.string.comments)
        viewModel.contentFilteredUpdated.observe(viewLifecycleOwner) { filterData ->
            if (filterData.isEmpty) {
                viewModel.fetchComments()
            } else {
                viewModel.data.value?.let {
                    adapter.submitList(
                        it.filter { item ->
                            if (item is CommentItem) {
                                item.comment.commentId !in filterData.filteredCommentIds &&
                                        item.comment.owner.userId !in filterData.filteredUserIds
                            } else {
                                true
                            }
                        }
                    )
                }
            }
        }
        viewModel.refreshing.observe(viewLifecycleOwner) { isRefreshing ->
            if (isRefreshing) {
                binding.loadingIndicator.show()
            }
        }
        viewModel.errorToast.observe(viewLifecycleOwner) { errorToast ->
            if (errorToast != null) {
                val message = when (errorToast) {
                    is CommentsViewModel.CommentError.AddCommentFailed ->
                        errorToast.reason ?: getString(R.string.add_comment_failure)
                    is CommentsViewModel.CommentError.UpvoteFailed -> errorToast.reason
                }
                Toast.makeText(
                    requireContext(),
                    message,
                    Toast.LENGTH_LONG,
                ).show()
            }
        }
        viewModel.data.observe(viewLifecycleOwner) {
            binding.loadingIndicator.hide()
            adapter.submitList(it)
            binding.header.subtitle.text = if (it.isNotEmpty()) {
                getString(R.string.item_count, it.filterIsInstance<CommentItem>().size)
            } else {
                getString(R.string.no_comments)
            }
            binding.emptySpace.isVisible = it.isEmpty()
        }
        viewModel.scrollToIndex.observe(viewLifecycleOwner) { index ->
            if (index != null && index != -1) {
                binding.recyclerView.scrollToPosition(index)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchComments()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
        }
        return dialog
    }

    companion object {
        private const val POST_ID = "post_id"
        private const val COMMENT_ID = "comment_id"

        fun show(
            fragmentManager: FragmentManager,
            postId: Int,
            commentId: Int? = null,
        ) {
            val fragment = CommentsBottomSheetDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(POST_ID, postId)
                    putInt(COMMENT_ID, commentId ?: -1)
                }
            }
            fragment.show(fragmentManager, CommentsBottomSheetDialogFragment::class.java.simpleName)
        }
    }
}
