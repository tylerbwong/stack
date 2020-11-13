package me.tylerbwong.stack.data.work

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkRequest

sealed class Work<T : WorkRequest>(
    internal val identifier: String,
    internal val workRequest: T
) {
    class OneTime(
        identifier: String,
        workRequest: OneTimeWorkRequest,
        internal val existingWorkPolicy: ExistingWorkPolicy
    ) : Work<OneTimeWorkRequest>(identifier, workRequest)

    class Periodic(
        identifier: String,
        workRequest: PeriodicWorkRequest,
        internal val existingWorkPolicy: ExistingPeriodicWorkPolicy
    ) : Work<PeriodicWorkRequest>(identifier, workRequest)
}
