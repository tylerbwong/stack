import org.gradle.api.Project

@Suppress("UNCHECKED_CAST")
fun <T> Project.property(key: String, default: T): T {
    return properties[key] as? T ?: default
}
