package me.tylerbwong.stack.ui.utils

import android.text.Spannable
import android.text.SpannableString

val noCopySpannableFactory = NoCopySpannableFactory()

class NoCopySpannableFactory : Spannable.Factory() {
    override fun newSpannable(source: CharSequence?): Spannable {
        return if (source is Spannable) {
            source
        } else {
            SpannableString(source)
        }
    }
}
