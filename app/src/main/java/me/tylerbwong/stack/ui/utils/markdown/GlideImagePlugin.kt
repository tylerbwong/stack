package me.tylerbwong.stack.ui.utils.markdown

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.NonNull
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import me.tylerbwong.stack.ui.utils.GlideApp
import ru.noties.markwon.AbstractMarkwonPlugin
import ru.noties.markwon.image.AsyncDrawable
import ru.noties.markwon.image.AsyncDrawableLoader
import ru.noties.markwon.image.DrawableUtils
import ru.noties.markwon.image.ImagesPlugin
import ru.noties.markwon.priority.Priority
import java.lang.ref.WeakReference
import java.util.Collections
import kotlin.collections.HashMap


class GlideImagePlugin private constructor(
        private val context: Context
) : AbstractMarkwonPlugin() {

    override fun configureImages(@NonNull builder: AsyncDrawableLoader.Builder) {
        builder.implementation(GlideAsyncDrawableLoader(context))
    }

    @NonNull
    override fun priority() = Priority.after(ImagesPlugin::class.java)

    private class GlideAsyncDrawableLoader (private val context: Context) : AsyncDrawableLoader() {
        private val cache = Collections.synchronizedMap(
                HashMap<String, WeakReference<AsyncDrawableTarget>>(3)
        )

        override fun load(@NonNull destination: String, @NonNull drawable: AsyncDrawable) {
            val target = AsyncDrawableTarget(drawable)
            GlideApp.with(context)
                    .load(destination)
                    .into(target)
            cache[destination] = WeakReference(target)
        }

        override fun cancel(@NonNull destination: String) {
            val reference = cache.remove(destination)
            val target = reference?.get()

            if (target != null) {
                GlideApp.with(context).clear(target)
                target.drawable.clearResult()
            }
        }

        override fun placeholder(): Nothing? = null

        private class AsyncDrawableTarget(
                internal val drawable: AsyncDrawable
        ) : CustomTarget<Drawable>() {

            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                DrawableUtils.applyIntrinsicBoundsIfEmpty(resource)
                drawable.result = resource
            }

            override fun onLoadStarted(placeholder: Drawable?) {
                if (placeholder != null) {
                    DrawableUtils.applyIntrinsicBoundsIfEmpty(placeholder)
                    drawable.result = placeholder
                }
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                if (errorDrawable != null) {
                    DrawableUtils.applyIntrinsicBoundsIfEmpty(errorDrawable)
                    drawable.result = errorDrawable
                }
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                drawable.clearResult()
            }
        }
    }

    companion object {
        fun create(context: Context) = GlideImagePlugin(context)
    }
}

