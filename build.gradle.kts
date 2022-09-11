buildscript {

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    val kotlinVersion: String by project
    val composeGradlePlugin: String by project
    val gradleVersion: String by project

    dependencies {
        classpath("org.jetbrains.compose:compose-gradle-plugin:$composeGradlePlugin")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.android.tools.build:gradle:$gradleVersion")
    }
}

allprojects {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

tasks.withType<Delete> {
    delete(rootProject.buildDir)
}