package me.tylerbwong.stack.ui.profile

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.bumptech.glide.request.RequestOptions
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.owners.BadgeView
import me.tylerbwong.stack.ui.utils.DynamicViewHolder
import me.tylerbwong.stack.ui.utils.GlideApp
import me.tylerbwong.stack.ui.utils.format
import me.tylerbwong.stack.ui.utils.inflateWithoutAttaching
import me.tylerbwong.stack.ui.utils.markdown.launchCustomTab

class ProfileHeaderHolder(parent: ViewGroup) : DynamicViewHolder(
        parent.inflateWithoutAttaching(R.layout.profile_header_holder)
) {
    private val userImage: ImageView = ViewCompat.requireViewById(itemView, R.id.userImage)
    private val username: TextView = ViewCompat.requireViewById(itemView, R.id.username)
    private val reputation: TextView = ViewCompat.requireViewById(itemView, R.id.reputation)
    private val badgeView: BadgeView = ViewCompat.requireViewById(itemView, R.id.badgeView)

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
                itemView.setOnClickListener {
                    launchCustomTab(it.context, link)
                }
            }
        }
    }
}
