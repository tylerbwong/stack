package me.tylerbwong.stack.ui.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.comments_fragment.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.utils.DynamicViewAdapter
import me.tylerbwong.stack.ui.utils.ViewHolderItemDecoration

class CommentsBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private val viewModel by viewModels<CommentsViewModel>()
    private val adapter = DynamicViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.postId = arguments?.getInt(POST_ID) ?: -1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.comments_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.apply {
            adapter = this@CommentsBottomSheetDialogFragment.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                ViewHolderItemDecoration(
                    context.resources.getDimensionPixelSize(R.dimen.item_spacing_main),
                    removeSideSpacing = true
                )
            )
        }
        viewModel.data.observe(viewLifecycleOwner) {
            adapter.update(it)
        }

        viewModel.fetchComments()
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
