package me.tylerbwong.stack.ui.owners

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import coil.api.load
import coil.transform.CircleCropTransformation
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.User
import me.tylerbwong.stack.databinding.OwnerViewBinding
import me.tylerbwong.stack.ui.profile.ProfileActivity
import me.tylerbwong.stack.ui.utils.format
import me.tylerbwong.stack.ui.utils.inflate
import me.tylerbwong.stack.ui.utils.ofType
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import me.tylerbwong.stack.ui.utils.toHtml

class OwnerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyle, defStyleRes) {

    private val binding = OwnerViewBinding.bind(
        inflate<ConstraintLayout>(R.layout.owner_view, attachToRoot = true)
    )

    fun bind(owner: User, isInCurrentSite: Boolean = true) {
        with(binding) {
            username.text = owner.displayName.toHtml()
            userImage.load(owner.profileImage) {
                crossfade(true)
                error(R.drawable.user_image_placeholder)
                placeholder(R.drawable.user_image_placeholder)
                transformations(CircleCropTransformation())
            }

            if (isInCurrentSite) {
                userImage.setThrottledOnClickListener {
                    val aoc = context.ofType<Activity>()?.let {
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            it,
                            Pair(
                                userImage,
                                context.getString(R.string.shared_transition_name)
                            )
                        )
                    }
                    ProfileActivity.startActivity(
                        context = context,
                        userId = owner.userId,
                        extras = aoc?.toBundle()
                    )
                }
            }
            badgeView.badgeCounts = owner.badgeCounts
            reputation.text = owner.reputation.toLong().format()
        }
    }
}
