package me.tylerbwong.stack.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.bitmap.BitmapPool
import coil.decode.DataSource
import coil.memory.MemoryCache
import coil.request.DefaultRequestOptions
import coil.request.Disposable
import coil.request.ImageRequest
import coil.request.ImageResult
import coil.request.SuccessResult

private val noOpMemoryCache = object : MemoryCache {
    override val maxSize = 0
    override val size = 0

    override fun clear() {
        // No-op
    }

    override fun get(key: MemoryCache.Key): Bitmap? = null

    override fun remove(key: MemoryCache.Key): Boolean = false

    override fun set(key: MemoryCache.Key, bitmap: Bitmap) {
        // No-op
    }
}

class TestImageLoader(private val context: Context) : ImageLoader {

    private val testDrawable = ColorDrawable(Color.CYAN)
    private val testErrorDrawable = ColorDrawable(Color.RED)
    @OptIn(ExperimentalCoilApi::class)
    private val noOpDisposable = object : Disposable {
        override val isDisposed = false

        @ExperimentalCoilApi
        override suspend fun await() {
            // No-op
        }

        override fun dispose() {
            // No-op
        }
    }

    override val bitmapPool = BitmapPool(maxSize = 0)
    override val defaults = DefaultRequestOptions(
        placeholder = testDrawable,
        error = testErrorDrawable
    )
    override val memoryCache = noOpMemoryCache

    override fun enqueue(request: ImageRequest): Disposable {
        request.target?.onStart(testDrawable)
        request.target?.onSuccess(testDrawable)
        return noOpDisposable
    }

    override suspend fun execute(request: ImageRequest): ImageResult {
        return SuccessResult(
            drawable = testDrawable,
            request = request,
            metadata = ImageResult.Metadata(
                memoryCacheKey = MemoryCache.Key(""),
                isSampled = false,
                dataSource = DataSource.MEMORY_CACHE,
                isPlaceholderMemoryCacheKeyPresent = false
            )
        )
    }

    override fun newBuilder(): ImageLoader.Builder = ImageLoader.Builder(context)

    override fun shutdown() {
        // No-op
    }
}
