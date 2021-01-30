package me.tylerbwong.stack.data.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tylerbwong.stack.data.repository.QuestionRepository
import timber.log.Timber

@HiltWorker
class BookmarksWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val questionRepository: QuestionRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = try {
        withContext(Dispatchers.IO) {
            questionRepository.syncBookmarks()
            Result.success()
        }
    } catch (ex: Exception) {
        Timber.e(ex)
        Result.failure()
    }

    companion object {
        internal const val IDENTIFIER = "bookmarks-worker"
    }
}
