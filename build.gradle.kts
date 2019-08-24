buildscript {
    repositories {
        google()
        jcenter()
        maven {
            setUrl("https://maven.fabric.io/public")
        }
    }
    dependencies {
        classpath(Dep.fabricPlugin)
        classpath(Dep.gradlePlugin)
        classpath(Dep.kotlinPlugin)
        classpath(Dep.googleServicesPlugin)
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
