import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        jcenter()
        fabric()
        gradle()
    }
    dependencies {
        classpath(Dep.fabricPlugin)
        classpath(Dep.gradlePlugin)
        classpath(Dep.kotlinPlugin)
        classpath(Dep.googleServicesPlugin)
        classpath(Dep.ktlintGradle)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
