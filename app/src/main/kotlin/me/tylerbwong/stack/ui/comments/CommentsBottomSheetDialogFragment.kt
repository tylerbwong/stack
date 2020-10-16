package me.tylerbwong.stack.ui.comments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.postId = arguments?.getInt(POST_ID) ?: -1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.comments_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = CommentsFragmentBinding.bind(view)
        binding.recyclerView.apply {
            adapter = this@CommentsBottomSheetDialogFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.header.title.text = getString(R.string.comments)
        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.header.subtitle.text = if (it.isNotEmpty()) {
                getString(R.string.item_count, it.size)
            } else {
                getString(R.string.no_comments)
            }
            binding.emptySpace.isVisible = it.isEmpty()
        }

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

        fun show(fragmentManager: FragmentManager, postId: Int) {
            val fragment = CommentsBottomSheetDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(POST_ID, postId)
                }
            }
            fragment.show(fragmentManager, CommentsBottomSheetDialogFragment::class.java.simpleName)
        }
    }
}
