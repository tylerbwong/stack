buildscript {
    repositories {
        kotlinEap()
        google()
        jcenter()
        gradle()
    }
    dependencies {
        classpath(Dep.androidGradlePlugin)
        classpath(Dep.firebaseCrashlyticsGradlePlugin)
        classpath(Dep.googleServicesPlugin)
        classpath(Dep.kotlinPlugin)
    }
}

allprojects {
    repositories {
        kotlinEap()
        google()
        jcenter()
    }

    // https://github.com/noties/Markwon/issues/148
    configurations.all {
        exclude(group = "org.jetbrains", module = "annotations-java5")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
