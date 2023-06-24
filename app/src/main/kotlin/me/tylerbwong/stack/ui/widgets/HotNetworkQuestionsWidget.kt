package me.tylerbwong.stack.ui.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
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
import timber.log.Timber
import java.util.concurrent.TimeUnit
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
            val question = getRandomHotNetworkQuestion(context)

            // Update widget views with the fetched question
            val remoteViews = buildRemoteViews(context, question)
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }
    }

    private suspend fun getHotNetworkQuestions(context: Context): List<NetworkHotQuestion> {
        val sharedPreferences = context.getSharedPreferences("hot_network_questions_widget_cache", Context.MODE_PRIVATE)
        val type = Types.newParameterizedType(MutableList::class.java, NetworkHotQuestion::class.java)
        val jsonAdapter = Moshi.Builder().build().adapter<List<NetworkHotQuestion>>(type)

        sharedPreferences.getString("hot_network_questions", null)?.let {
            val expiresAfter = sharedPreferences.getLong("hot_network_questions_expires_after", -1)

            if (expiresAfter > System.currentTimeMillis()) {
                Timber.d("hot network questions: cache hit")

                // todo: we should actually probably not count null or an empty list as a cache hit
                return jsonAdapter.fromJson(it) ?: emptyList()
            }
        }

        Timber.d("hot network questions: cache miss")

        return networkRepository.getHotNetworkQuestions().also {
            sharedPreferences.edit().apply {
                putString("hot_network_questions", jsonAdapter.toJson(it))
                putLong(
                    "hot_network_questions_expires_after",
                    System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(CACHE_EXPIRES_AFTER_MINUTES)
                )

                apply()
            }
        }
    }

    private suspend fun getRandomHotNetworkQuestion(context: Context): NetworkHotQuestion {
        // todo: avoid picking the same hot question in a row (use setExtra)
        return withContext(ioDispatcher) {
            getHotNetworkQuestions(context)
                .also { Timber.d("hot network questions count is ${it.size}") }
                .random()
        }
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
        private const val CACHE_EXPIRES_AFTER_MINUTES = 5L
    }
}
