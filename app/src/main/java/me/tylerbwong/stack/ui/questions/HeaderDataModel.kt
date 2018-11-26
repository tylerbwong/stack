package me.tylerbwong.stack.ui.questions

import me.tylerbwong.stack.ui.utils.DynamicDataModel

class HeaderDataModel(internal val title: String, internal val subtitle: String) : DynamicDataModel() {

    override fun areItemsThemSame(other: DynamicDataModel) = other is HeaderDataModel

    override fun areContentsTheSame(other: DynamicDataModel) =
            other is HeaderDataModel && title == other.title && subtitle == other.subtitle

    override fun getViewCreator() = ::HeaderHolder
}
