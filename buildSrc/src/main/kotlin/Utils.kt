import org.gradle.api.Project

fun Project.stringProperty(key: String, default: String = "\"\""): String = property(key, default)

@Suppress("UNCHECKED_CAST")
fun <T> Project.property(key: String, default: T): T {
    return properties[key] as? T ?: default
}
