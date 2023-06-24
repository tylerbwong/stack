package me.tylerbwong.stack.ui.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.NetworkHotQuestion
import me.tylerbwong.stack.data.repository.NetworkRepository
import javax.inject.Inject

@AndroidEntryPoint
class HotNetworkQuestionsWidget @OptIn(DelicateCoroutinesApi::class) constructor(
    // TODO there's probably a better way to coroutine this...
    private val externalScope: CoroutineScope = GlobalScope,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AppWidgetProvider() {
    @Inject
    lateinit var networkRepository: NetworkRepository

    // Handle widget refresh action
    private fun refreshWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        externalScope.launch {
            val question = getRandomHotNetworkQuestion()

            // Update widget views with the fetched question
            val remoteViews = buildRemoteViews(context, question)
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }
    }

    private suspend fun getRandomHotNetworkQuestion(): NetworkHotQuestion {
        // todo: avoid picking the same hot question in a row (use setExtra)
        // todo: we should catch this list for a short time
        return withContext(ioDispatcher) { networkRepository.getHotNetworkQuestions().random() }
    }

    // Handle widget question click action
    private fun openQuestion(context: Context) {
        // TODO: Implement the logic to open the tapped question in the Stack Android app
        Toast.makeText(context, "Opening question...", Toast.LENGTH_SHORT).show()
    }

    // Build the remote views for the widget
    private fun buildRemoteViews(context: Context, question: NetworkHotQuestion): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.hot_network_questions_widget)

        // Set the question title
        remoteViews.setTextViewText(R.id.questionTitleTextView, question.title)

        // Set click listeners for the question title and refresh button
        remoteViews.setOnClickPendingIntent(R.id.questionTitleTextView, getPendingIntent(context, ACTION_OPEN_QUESTION))
        remoteViews.setOnClickPendingIntent(R.id.refreshButton, getPendingIntent(context, ACTION_REFRESH))

        return remoteViews
    }

    // Create a pending intent for the specified action
    private fun getPendingIntent(context: Context, action: String): PendingIntent {
        val intent = Intent(context, HotNetworkQuestionsWidget::class.java)
        intent.action = action
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Iterate through each widget instance
        for (appWidgetId in appWidgetIds) {
            refreshWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        when (intent.action) {
            ACTION_REFRESH -> {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(
                    ComponentName(context, HotNetworkQuestionsWidget::class.java)
                )

                // Iterate through each widget instance and refresh
                for (appWidgetId in appWidgetIds) {
                    refreshWidget(context, appWidgetManager, appWidgetId)
                }
            }

            ACTION_OPEN_QUESTION -> {
                openQuestion(context)
            }
        }
    }

    companion object {
        private const val ACTION_REFRESH = "me.tylerbwong.stack.widget.ACTION_REFRESH"
        private const val ACTION_OPEN_QUESTION = "me.tylerbwong.stack.widget.ACTION_OPEN_QUESTION"
    }
}
