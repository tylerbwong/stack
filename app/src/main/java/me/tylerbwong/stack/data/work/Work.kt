package me.tylerbwong.stack.data.work

import me.tylerbwong.stack.ui.ApplicationWrapper

object Work {

    private val workManager = ApplicationWrapper.stackComponent.workManager()
    private val workRequest = ApplicationWrapper.stackComponent.sitesWorkRequest()

    fun schedule() {
        workManager.enqueue(workRequest)
    }
}
