package me.tylerbwong.stack.ui.shortcuts

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.MainActivity
import me.tylerbwong.stack.ui.questions.ask.AskQuestionActivity

private const val SHORTCUT_ASK_QUESTION = "shortcut_ask_question"

fun Context.pushAskQuestionShortcut() {
    val shortcut = ShortcutInfoCompat.Builder(this, SHORTCUT_ASK_QUESTION)
        .setShortLabel(getString(R.string.ask_question))
        .setIcon(IconCompat.createWithResource(this, R.drawable.ic_shortcut_add_circle))
        .setIntents(
            arrayOf(
                MainActivity.makeIntentClearTop(this)
                    .setAction(Intent.ACTION_VIEW),
                Intent(this, AskQuestionActivity::class.java)
                    .setAction(Intent.ACTION_VIEW)
            )
        )
        .build()
    ShortcutManagerCompat.pushDynamicShortcut(this, shortcut)
}

fun Context.removeAskQuestionShortcut() {
    ShortcutManagerCompat.removeDynamicShortcuts(this, listOf(SHORTCUT_ASK_QUESTION))
}
