package me.tylerbwong.stack.ui.questions

import me.tylerbwong.stack.ui.utils.DynamicDataModel

data class HeaderDataModel(
        internal val title: String,
        internal val subtitle: String
) : DynamicDataModel() {

    override fun areItemsThemSame(other: DynamicDataModel) = other == this

    override fun areContentsTheSame(other: DynamicDataModel) = other == this

    override fun getViewCreator() = ::HeaderHolder
}
