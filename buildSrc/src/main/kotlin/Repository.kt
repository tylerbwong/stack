import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.maven

fun RepositoryHandler.gradle() = maven(url = "https://plugins.gradle.org/m2/")
