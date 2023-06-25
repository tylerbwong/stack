package me.tylerbwong.stack.ui.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.RemoteViews
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
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import timber.log.Timber
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
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

    private fun refreshWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            externalScope.launch {
                val question = getRandomHotNetworkQuestion(context)

                val remoteViews = buildRemoteViews(context, question)
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
            }
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

    private fun buildRemoteViews(context: Context, question: NetworkHotQuestion): RemoteViews {
        return RemoteViews(context.packageName, R.layout.hot_network_questions_widget).apply {
            // Set the question title
            setTextViewText(R.id.questionTitleTextView, question.title)

            // todo: it looks like we should try and use Coil for this?
            try {
                val iconBitmap = fetchQuestionIcon(question)
                setImageViewBitmap(R.id.hotQuestionIcon, iconBitmap)
                setViewVisibility(R.id.hotQuestionIcon, View.VISIBLE)
            } catch (e: Exception) {
                setViewVisibility(R.id.hotQuestionIcon, View.INVISIBLE)
                e.printStackTrace()
            }

            // Set click listeners for the question title and refresh button
            setOnClickPendingIntent(R.id.questionTitleTextView, getOpenQuestionIntent(context, question))
            setOnClickPendingIntent(R.id.fetchNewHotQuestionButton, getFetchNewHotQuestionIntent(context))
        }
    }

    private fun getOpenQuestionIntent(context: Context, question: NetworkHotQuestion): PendingIntent {
        val intent = QuestionDetailActivity.makeIntent(
            context = context,
            questionId = question.questionId,
            deepLinkSite = question.site,
            clearDeepLinkedSites = true
        )

        return PendingIntent.getActivity(
            context,
            System.currentTimeMillis().toInt(),
            intent.setAction("OPEN_QUESTION_${question.questionId}_ON_${question.site}"),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getFetchNewHotQuestionIntent(context: Context): PendingIntent {
        val intent = Intent(context, HotNetworkQuestionsWidget::class.java)
        intent.action = ACTION_REFRESH

        return PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun fetchQuestionIcon(question: NetworkHotQuestion): Bitmap? {
        var inputStream: InputStream? = null
        var connection: HttpURLConnection? = null
        return try {
            val url = URL(question.iconUrl)
            connection = url.openConnection() as HttpURLConnection
            connection.connect()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.inputStream
                BitmapFactory.decodeStream(inputStream)
            } else {
                Timber.d("got " + connection.responseCode)
                null
            }
        } catch (e: Exception) {
            Timber.d(e)
            null
        } finally {
            inputStream?.close()
            connection?.disconnect()
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        refreshWidgets(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        when (intent.action) {
            ACTION_REFRESH -> {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(
                    ComponentName(context, HotNetworkQuestionsWidget::class.java)
                )

                refreshWidgets(context, appWidgetManager, appWidgetIds)
            }
        }
    }

    companion object {
        private const val ACTION_REFRESH = "me.tylerbwong.stack.widget.ACTION_REFRESH"
        private const val CACHE_EXPIRES_AFTER_MINUTES = 5L
    }
}
