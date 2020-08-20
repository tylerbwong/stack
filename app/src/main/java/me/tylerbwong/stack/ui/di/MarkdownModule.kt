package me.tylerbwong.stack.ui.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.noties.markwon.Markwon
import io.noties.markwon.PrecomputedFutureTextSetterCompat
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.coil.CoilImagesPlugin
import io.noties.markwon.linkify.LinkifyPlugin
import io.noties.markwon.syntax.Prism4jThemeBase
import io.noties.markwon.syntax.Prism4jThemeDarkula
import io.noties.markwon.syntax.Prism4jThemeDefault
import io.noties.markwon.syntax.SyntaxHighlightPlugin
import io.noties.prism4j.Prism4j
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.settings.Experimental
import me.tylerbwong.stack.ui.theme.ThemeManager.isNightModeEnabled
import me.tylerbwong.stack.ui.utils.markdown.GrammarLocatorDef
import me.tylerbwong.stack.ui.utils.markdown.UrlPlugin
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MarkdownModule {

    @Provides
    fun provideCoilImagesPlugin(context: Context) = CoilImagesPlugin.create(context)

    @Provides
    fun provideHtmlPlugin() = HtmlPlugin.create()

    @Provides
    fun provideLinkifyPlugin() = LinkifyPlugin.create()

    @Provides
    fun provideStrikethroughPlugin() = StrikethroughPlugin.create()

    @Provides
    fun provideTablePlugin(context: Context) = TablePlugin.create(context)

    @Provides
    fun provideTaskListPlugin(context: Context) = TaskListPlugin.create(
        context.getColor(R.color.colorAccent),
        context.getColor(R.color.colorAccent),
        context.getColor(R.color.white)
    )

    @Provides
    fun providePrism4j() = Prism4j(GrammarLocatorDef())

    @Provides
    fun providePrism4jTheme(context: Context): Prism4jThemeBase = if (context.isNightModeEnabled) {
        Prism4jThemeDarkula.create()
    } else {
        Prism4jThemeDefault.create()
    }

    @Provides
    fun provideSyntaxHighlightPlugin(
        prism4j: Prism4j,
        prism4jTheme: Prism4jThemeBase
    ) = SyntaxHighlightPlugin.create(prism4j, prism4jTheme)

    @Provides
    fun provideExecutor(): Executor = Executors.newCachedThreadPool()

    @Provides
    fun provideTextSetter(
        executor: Executor
    ): Markwon.TextSetter = PrecomputedFutureTextSetterCompat.create(executor)

    @Suppress("LongParameterList")
    @Singleton
    @Provides
    fun provideMarkwon(
        context: Context,
        experimental: Experimental,
        coilImagesPlugin: CoilImagesPlugin,
        htmlPlugin: HtmlPlugin,
        linkifyPlugin: LinkifyPlugin,
        strikethroughPlugin: StrikethroughPlugin,
        tablePlugin: TablePlugin,
        taskListPlugin: TaskListPlugin,
        urlPlugin: UrlPlugin,
        syntaxHighlightPlugin: SyntaxHighlightPlugin,
        textSetter: Markwon.TextSetter
    ): Markwon {
        val plugins = listOf(
            coilImagesPlugin,
            htmlPlugin,
            linkifyPlugin,
            strikethroughPlugin,
            tablePlugin,
            taskListPlugin,
            urlPlugin
        )
        val experimentalPlugins = if (experimental.syntaxHighlightingEnabled) {
            listOf(syntaxHighlightPlugin)
        } else {
            emptyList()
        }

        return Markwon.builder(context)
            .usePlugins(plugins + experimentalPlugins)
            .textSetter(textSetter)
            .build()
    }
}
