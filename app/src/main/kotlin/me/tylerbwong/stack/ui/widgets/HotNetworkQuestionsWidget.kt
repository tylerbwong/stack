package me.tylerbwong.stack.ui.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import me.tylerbwong.stack.R

class HotNetworkQuestionsWidget : AppWidgetProvider() {
    // Handle widget refresh action
    private fun refreshWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        // TODO: Implement the logic to fetch a random hot question from the Stack Exchange API

        // Update widget views with the fetched question
        val remoteViews = buildRemoteViews(context, getRandomTitle())
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
    }

    private fun getRandomTitle(): String {
        val titles = arrayOf(
            "What is the best programming language for beginners?",
            "How to optimize SQL queries for better performance?",
            "What are the key principles of object-oriented programming?",
            "How does encryption work in network security?",
            "What are the latest trends in artificial intelligence?",
            "How to handle exceptions in Java programming?",
            "What are the differences between HTTP and HTTPS?",
            "What are the best practices for mobile app development?",
            "How to implement data caching in Android applications?",
            "What are the advantages of using a NoSQL database?"
        )
        val randomIndex = titles.indices.random()

        return titles[randomIndex]
    }

    // Handle widget question click action
    private fun openQuestion(context: Context) {
        // TODO: Implement the logic to open the tapped question in the Stack Android app
        Toast.makeText(context, "Opening question...", Toast.LENGTH_SHORT).show()
    }

    // Build the remote views for the widget
    private fun buildRemoteViews(context: Context, questionTitle: String): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.hot_network_questions_widget)

        // Set the question title
        remoteViews.setTextViewText(R.id.questionTitleTextView, questionTitle)

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
