package me.tylerbwong.stack.ui.questions.create

import android.os.Bundle
import android.view.View
import androidx.compose.Recomposer
import androidx.fragment.app.viewModels
import androidx.ui.core.setContent
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.stack.databinding.CreateQuestionFragmentBinding
import me.tylerbwong.stack.ui.BaseFragment

@AndroidEntryPoint
class CreateQuestionFragment : BaseFragment<CreateQuestionFragmentBinding>(
    CreateQuestionFragmentBinding::inflate
) {
    private val viewModel by viewModels<CreateQuestionViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.composeContent.setContent(Recomposer.current()) {
            CreateQuestionLayout(
                onCreateQuestion = viewModel::createQuestion,
                onBackPressed = { requireActivity().onBackPressed() }
            )
        }
    }
}
