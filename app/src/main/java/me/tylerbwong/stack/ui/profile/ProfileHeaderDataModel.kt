package me.tylerbwong.stack.ui.profile

import me.tylerbwong.stack.data.model.User
import me.tylerbwong.stack.ui.utils.DynamicDataModel

class ProfileHeaderDataModel(user: User) : DynamicDataModel() {

    internal val userImage = user.profileImage
    internal val username = user.displayName
    internal val reputation = user.reputation
    internal val badgeCounts = user.badgeCounts
    internal val link = user.link
    private val userId = user.userId

    override fun areItemsThemSame(
            other: DynamicDataModel
    ) = other is ProfileHeaderDataModel && other.userId == userId

    override fun areContentsTheSame(
            other: DynamicDataModel
    ) = other is ProfileHeaderDataModel && other.userImage == userImage
            && other.username == username && other.reputation == reputation
            && other.badgeCounts == badgeCounts

    override fun getViewCreator() = ::ProfileHeaderHolder
}
