package me.tylerbwong.stack.ui.utils.markdown

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spanned
import android.widget.TextView
import coil.Coil
import coil.api.load
import coil.request.RequestDisposable
import coil.target.Target
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.MarkwonSpansFactory
import io.noties.markwon.image.AsyncDrawable
import io.noties.markwon.image.AsyncDrawableLoader
import io.noties.markwon.image.AsyncDrawableScheduler
import io.noties.markwon.image.DrawableUtils
import io.noties.markwon.image.ImageSpanFactory
import org.commonmark.node.Image
import java.util.HashMap

class CoilImagePlugin private constructor(context: Context) : AbstractMarkwonPlugin() {

    private val coilAsyncDrawableLoader = CoilAsyncDrawableLoader(context)

    override fun configureSpansFactory(builder: MarkwonSpansFactory.Builder) {
        builder.setFactory(Image::class.java, ImageSpanFactory())
    }

    override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
        builder.asyncDrawableLoader(coilAsyncDrawableLoader)
    }

    override fun beforeSetText(textView: TextView, markdown: Spanned) {
        AsyncDrawableScheduler.unschedule(textView)
    }

    override fun afterSetText(textView: TextView) {
        AsyncDrawableScheduler.schedule(textView)
    }

    private class CoilAsyncDrawableLoader internal constructor(
        private val context: Context
    ) : AsyncDrawableLoader() {
        private val cache = HashMap<AsyncDrawable, RequestDisposable>(2)

        override fun load(drawable: AsyncDrawable) {
            val target = AsyncDrawableTarget(drawable)
            cache[drawable] = Coil.load(context, drawable.destination) {
                target(target)
            }
        }

        override fun cancel(drawable: AsyncDrawable) {
            val disposable = cache.remove(drawable)
            disposable?.dispose()
        }

        override fun placeholder(drawable: AsyncDrawable): Drawable? = null

        private inner class AsyncDrawableTarget internal constructor(
            private val drawable: AsyncDrawable
        ) : Target {
            override fun onSuccess(result: Drawable) {
                if (cache.remove(drawable) != null) {
                    if (drawable.isAttached) {
                        DrawableUtils.applyIntrinsicBoundsIfEmpty(result)
                        drawable.result = result
                    }
                }
            }

            override fun onStart(placeholder: Drawable?) {
                if (placeholder != null && drawable.isAttached) {
                    DrawableUtils.applyIntrinsicBoundsIfEmpty(placeholder)
                    drawable.result = placeholder
                }
            }

            override fun onError(error: Drawable?) {
                if (cache.remove(drawable) != null) {
                    if (error != null && drawable.isAttached) {
                        DrawableUtils.applyIntrinsicBoundsIfEmpty(error)
                        drawable.result = error
                    }
                }
            }
        }
    }

    companion object {
        fun create(context: Context) = CoilImagePlugin(context)
    }
}
