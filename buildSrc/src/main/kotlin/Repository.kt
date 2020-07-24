import org.gradle.api.artifacts.dsl.RepositoryHandler

fun RepositoryHandler.gradle() = maven {
    setUrl("https://plugins.gradle.org/m2/")
}
