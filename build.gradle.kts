import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    val kotlin_version by extra("1.3.72")
    repositories {
        google()
        jcenter()
        gradle()
    }
    dependencies {
        classpath(Dep.androidGradlePlugin)
        classpath(Dep.kotlinPlugin)
        classpath(Dep.googleServicesPlugin)
        classpath(Dep.firebaseCrashlyticsGradlePlugin)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }

    // https://github.com/noties/Markwon/issues/148
    configurations.all {
        exclude(group = "org.jetbrains", module = "annotations-java5")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        apiVersion = "1.3"
        jvmTarget = "1.8"
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
