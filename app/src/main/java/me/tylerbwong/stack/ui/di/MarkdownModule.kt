package me.tylerbwong.stack.ui.di

import android.content.Context
import dagger.Module
import dagger.Provides
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.coil.CoilImagesPlugin
import io.noties.markwon.syntax.Prism4jThemeDarkula
import io.noties.markwon.syntax.SyntaxHighlightPlugin
import io.noties.prism4j.Prism4j
import me.tylerbwong.stack.data.DeepLinker
import me.tylerbwong.stack.ui.utils.markdown.CustomTabsLinkResolver
import me.tylerbwong.stack.ui.utils.markdown.CustomUrlProcessor
import me.tylerbwong.stack.ui.utils.markdown.GrammarLocatorDef
import me.tylerbwong.stack.ui.utils.markdown.Markdown
import me.tylerbwong.stack.ui.utils.markdown.UrlPlugin
import javax.inject.Singleton

@Module
class MarkdownModule {

    @Provides
    fun provideCoilImagesPlugin(context: Context) = CoilImagesPlugin.create(context)

    @Provides
    fun provideHtmlPlugin() = HtmlPlugin.create()

    @Provides
    fun provideStrikethroughPlugin() = StrikethroughPlugin.create()

    @Provides
    fun provideTablePlugin(context: Context) = TablePlugin.create(context)

    @Provides
    fun provideUrlProcessor() = CustomUrlProcessor()

    @Provides
    fun provideTabsLinkResolver(deepLinker: DeepLinker) = CustomTabsLinkResolver(deepLinker)

    @Provides
    fun provideUrlPlugin(
        urlProcessor: CustomUrlProcessor,
        tabsLinkResolver: CustomTabsLinkResolver
    ) = UrlPlugin(urlProcessor, tabsLinkResolver)

    @Provides
    fun providePrism4j() = Prism4j(GrammarLocatorDef())

    @Provides
    fun providePrism4jThemeDarkula() = Prism4jThemeDarkula.create()

    @Provides
    fun provideSyntaxHighlightPlugin(
        prism4j: Prism4j,
        prism4jThemeDarkula: Prism4jThemeDarkula
    ) = SyntaxHighlightPlugin.create(prism4j, prism4jThemeDarkula)

    @Singleton
    @Provides
    fun provideMarkwon(
        context: Context,
        coilImagesPlugin: CoilImagesPlugin,
        htmlPlugin: HtmlPlugin,
        strikethroughPlugin: StrikethroughPlugin,
        tablePlugin: TablePlugin,
        urlPlugin: UrlPlugin,
        syntaxHighlightPlugin: SyntaxHighlightPlugin
    ): Markwon {
        val plugins = listOf(
            coilImagesPlugin,
            htmlPlugin,
            strikethroughPlugin,
            tablePlugin,
            urlPlugin
        )
        val experimentalPlugins = if (Markdown.experimentalSyntaxHighlightingEnabled) {
            listOf(syntaxHighlightPlugin)
        } else {
            emptyList()
        }

        return Markwon.builder(context)
            .usePlugins(plugins + experimentalPlugins)
            .build()
    }
}
