package me.tylerbwong.stack.latex

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LatexTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    @Inject
    lateinit var latex: Latex

    fun setLatex(text: String) = latex.setLatex(this, text)
}
