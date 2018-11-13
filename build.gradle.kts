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
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
