package me.tylerbwong.stack.ui.profile

import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.profile_header_holder.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.utils.DynamicViewHolder
import me.tylerbwong.stack.ui.utils.GlideApp
import me.tylerbwong.stack.ui.utils.format
import me.tylerbwong.stack.ui.utils.inflateWithoutAttaching
import me.tylerbwong.stack.ui.utils.markdown.launchCustomTab
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener

class ProfileHeaderHolder(parent: ViewGroup) : DynamicViewHolder(
        parent.inflateWithoutAttaching(R.layout.profile_header_holder)
) {
    override fun bind(data: Any) {
        (data as? ProfileHeaderDataModel)?.let { dataModel ->
            GlideApp.with(itemView)
                    .load(dataModel.userImage)
                    .placeholder(R.drawable.user_image_placeholder)
                    .apply(RequestOptions.circleCropTransform())
                    .into(userImage)
            username.text = dataModel.username
            reputation.text = dataModel.reputation.toLong().format()
            badgeView.badgeCounts = dataModel.badgeCounts

            dataModel.link?.let { link ->
                itemView.setThrottledOnClickListener {
                    launchCustomTab(it.context, link)
                }
            }
        }
    }
}
