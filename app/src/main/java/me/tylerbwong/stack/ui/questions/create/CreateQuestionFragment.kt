package me.tylerbwong.stack.ui.questions.create

import android.os.Bundle
import android.view.View
import androidx.compose.Recomposer
import androidx.fragment.app.viewModels
import androidx.ui.core.setContent
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.stack.databinding.CreateQuestionFragmentBinding
import me.tylerbwong.stack.ui.BaseFragment
import me.tylerbwong.stack.ui.questions.create.CreateQuestionActivity.Companion.DRAFT_ID

@AndroidEntryPoint
class CreateQuestionFragment : BaseFragment<CreateQuestionFragmentBinding>(
    CreateQuestionFragmentBinding::inflate
) {
    private val viewModel by viewModels<CreateQuestionViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.composeContent.setContent(Recomposer.current()) {
            CreateQuestionLayout(
                draftLiveData = viewModel.questionDraft,
                createQuestion = viewModel::createQuestion,
                saveDraft = viewModel::saveDraft,
                onBackPressed = { requireActivity().onBackPressed() }
            )
        }
        viewModel.fetchDraft(arguments?.getInt(DRAFT_ID) ?: -1)
    }

    companion object {
        fun newInstance(id: Int): CreateQuestionFragment {
            val fragment = CreateQuestionFragment()
            val bundle = Bundle().apply { putInt(DRAFT_ID, id) }
            fragment.arguments = bundle
            return fragment
        }
    }
}
