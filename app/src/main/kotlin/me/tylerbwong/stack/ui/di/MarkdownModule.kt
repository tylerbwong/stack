package me.tylerbwong.stack.ui.di

import android.content.Context
import androidx.core.content.ContextCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet
import dagger.multibindings.IntoSet
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.PrecomputedTextSetterCompat
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.coil.CoilImagesPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin
import io.noties.markwon.linkify.LinkifyPlugin
import io.noties.markwon.movement.MovementMethodPlugin
import io.noties.markwon.syntax.Prism4jThemeBase
import io.noties.markwon.syntax.Prism4jThemeDarkula
import io.noties.markwon.syntax.Prism4jThemeDefault
import io.noties.markwon.syntax.SyntaxHighlightPlugin
import io.noties.prism4j.Prism4j
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import me.tylerbwong.stack.R
import me.tylerbwong.stack.latex.LatexMarkdown
import me.tylerbwong.stack.markdown.GrammarLocatorDef
import me.tylerbwong.stack.markdown.MarkdownMarkwon
import me.tylerbwong.stack.ui.settings.Experimental
import me.tylerbwong.stack.ui.theme.ThemeManager.isNightModeEnabled
import me.tylerbwong.stack.ui.utils.markdown.CustomTabsLinkResolver
import me.tylerbwong.stack.ui.utils.markdown.CustomUrlProcessor
import me.tylerbwong.stack.ui.utils.markdown.LatexInlineProcessor
import me.tylerbwong.stack.ui.utils.markdown.UrlPlugin
import java.util.concurrent.Executor
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MarkwonPlugin

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SharedMarkwonPlugin

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ExperimentalMarkwonPlugin

@Module
@InstallIn(SingletonComponent::class)
class MarkdownModule {

    @[Provides IntoSet MarkwonPlugin]
    fun provideCoilImagesPlugin(
        @ApplicationContext context: Context
    ): AbstractMarkwonPlugin = CoilImagesPlugin.create(context)

    @[Provides IntoSet MarkwonPlugin]
    fun provideHtmlPlugin(): AbstractMarkwonPlugin = HtmlPlugin.create()

    @[Provides IntoSet SharedMarkwonPlugin]
    fun provideInlinePlugin(
        latexInlineProcessor: LatexInlineProcessor
    ): AbstractMarkwonPlugin = MarkwonInlineParserPlugin.create {
        it.addInlineProcessor(latexInlineProcessor)
    }

    @[Provides IntoSet SharedMarkwonPlugin]
    fun provideLatexPlugin(
        @ApplicationContext context: Context
    ): AbstractMarkwonPlugin = JLatexMathPlugin.create(
        context.resources.getDimension(R.dimen.inline_latex_text_size),
        context.resources.getDimension(R.dimen.block_latex_text_size)
    ) { it.inlinesEnabled(true) }

    @[Provides IntoSet MarkwonPlugin]
    fun provideLinkifyPlugin(): AbstractMarkwonPlugin = LinkifyPlugin.create()

    @[Provides IntoSet MarkwonPlugin]
    fun provideStrikethroughPlugin(): AbstractMarkwonPlugin = StrikethroughPlugin.create()

    @[Provides ElementsIntoSet ExperimentalMarkwonPlugin]
    fun provideSyntaxHighlightPlugin(
        experimental: Experimental,
        prism4j: Prism4j,
        prism4jTheme: Prism4jThemeBase
    ): Set<AbstractMarkwonPlugin> = if (experimental.syntaxHighlightingEnabled) {
        setOf(SyntaxHighlightPlugin.create(prism4j, prism4jTheme))
    } else {
        emptySet()
    }

    @[Provides IntoSet MarkwonPlugin]
    fun provideTablePlugin(
        @ApplicationContext context: Context
    ): AbstractMarkwonPlugin = TablePlugin.create(context)

    @[Provides IntoSet MarkwonPlugin]
    fun provideTaskListPlugin(
        @ApplicationContext context: Context
    ): AbstractMarkwonPlugin = TaskListPlugin.create(
        ContextCompat.getColor(context, R.color.colorAccent),
        ContextCompat.getColor(context, R.color.colorAccent),
        ContextCompat.getColor(context, R.color.white)
    )

    @[Provides IntoSet MarkwonPlugin]
    fun provideUrlPlugin(
        urlProcessor: CustomUrlProcessor,
        tabsResolver: CustomTabsLinkResolver
    ): AbstractMarkwonPlugin = UrlPlugin(urlProcessor, tabsResolver)

    @Provides
    fun providePrism4j() = Prism4j(GrammarLocatorDef())

    @Provides
    fun providePrism4jTheme(
        @ApplicationContext context: Context
    ): Prism4jThemeBase = if (context.isNightModeEnabled) {
        Prism4jThemeDarkula.create()
    } else {
        Prism4jThemeDefault.create()
    }

    @Provides
    fun provideExecutor(): Executor = Dispatchers.Default.asExecutor()

    @Provides
    fun provideTextSetter(
        executor: Executor
    ): Markwon.TextSetter = PrecomputedTextSetterCompat.create(executor)

    @[Provides Singleton MarkdownMarkwon]
    fun provideMarkdownMarkwon(
        @ApplicationContext context: Context,
        @MarkwonPlugin plugins: Set<@JvmSuppressWildcards AbstractMarkwonPlugin>,
        @SharedMarkwonPlugin sharedPlugins: Set<@JvmSuppressWildcards AbstractMarkwonPlugin>,
        @ExperimentalMarkwonPlugin experimentalPlugins: Set<@JvmSuppressWildcards AbstractMarkwonPlugin>,
//        textSetter: Markwon.TextSetter
    ): Markwon {
        return Markwon.builder(context)
            .usePlugins(plugins + sharedPlugins + experimentalPlugins)
//            .textSetter(textSetter) /* Causing stuttering in [RecyclerView] */
            .build()
    }

    @[Provides Singleton LatexMarkdown]
    fun provideLatexMarkwon(
        @ApplicationContext context: Context,
        @SharedMarkwonPlugin sharedPlugins: Set<@JvmSuppressWildcards AbstractMarkwonPlugin>,
//        textSetter: Markwon.TextSetter
    ): Markwon {
        return Markwon.builder(context)
            .usePlugin(MovementMethodPlugin.none())
            .usePlugins(sharedPlugins)
//            .textSetter(textSetter) /* Causing stuttering in [RecyclerView] */
            .build()
    }
}
