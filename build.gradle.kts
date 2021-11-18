buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://jitpack.io")
    }
    dependencies {
        classpath(Build.gradle)
        classpath(Build.kotlin)
        classpath(Build.jUnit5)
        classpath(Build.hilt)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}