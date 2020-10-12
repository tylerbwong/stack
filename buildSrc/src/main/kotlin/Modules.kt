import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project

fun DependencyHandlerScope.composeMarkdown() = project(":compose-markdown")
fun DependencyHandlerScope.composePreference() = project(":compose-preference")
fun DependencyHandlerScope.dynamicListAdapter() = project(":dynamic-list-adapter")
fun DependencyHandlerScope.dynamicListAdapterExtensions() = project(":dynamic-list-adapter-extensions")
fun DependencyHandlerScope.dynamicListAdapterViewBinding() = project(":dynamic-list-adapter-viewbinding")
fun DependencyHandlerScope.markdown() = project(":markdown")
fun DependencyHandlerScope.stackExchangeApi() = project(":stackexchange-api")
