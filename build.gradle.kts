buildscript {
    repositories {
        google()
        jcenter()
        gradle()
    }
    dependencies {
        classpath(Dep.androidGradlePlugin)
        classpath(Dep.daggerHiltGradlePlugin)
        classpath(Dep.detektGradlePlugin)
        classpath(Dep.firebaseCrashlyticsGradlePlugin)
        classpath(Dep.googleServicesPlugin)
        classpath(Dep.kotlinPlugin)
        classpath(Dep.ktlintGradlePlugin)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }

    configurations.all {
        // https://github.com/noties/Markwon/issues/148
        exclude(group = "org.jetbrains", module = "annotations-java5")

        resolutionStrategy {
            force(Dep.androidxAppCompat) // Pin version for night mode bug
            force(Dep.coil)
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
