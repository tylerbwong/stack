import org.gradle.api.artifacts.dsl.RepositoryHandler

fun RepositoryHandler.fabric() = maven {
    setUrl("https://maven.fabric.io/public")
}

fun RepositoryHandler.gradle() = maven {
    setUrl("https://plugins.gradle.org/m2/")
}
