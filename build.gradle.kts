buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(Dep.gradlePlugin)
        classpath(Dep.kotlinPlugin)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
