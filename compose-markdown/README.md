# compose-markdown

This library provides a Jetpack Compose Text component `MarkdownText` for rendering markdown in a `@Composable` hierarchy. It follows the Commonmark specification and supports GitHub flavored markdown elements as well.

It uses [intellij-markdown](https://github.com/valich/intellij-markdown) under the hood for parsing markdown content into an abstract syntax tree and then uses the [`AnnotatedString`](https://developer.android.com/reference/kotlin/androidx/compose/ui/text/AnnotatedString.html) API to display the parsed content onto a `Text` composable.

In order to display inline content such as images, links, block quotes, checkboxes, etc. this library uses the [`InlineTextContent`](https://developer.android.com/reference/kotlin/androidx/compose/foundation/text/InlineTextContent) API to insert `@Composable` hierarchies within a `Text` composable itself.

Check out [compose-markdown-demo](https://github.com/tylerbwong/stack/tree/master/compose-markdown-demo) for examples usages!
