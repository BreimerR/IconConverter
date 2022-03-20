import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.compose") version "1.1.1"
}

group = "libetal.web.test"
version = "1.0"

repositories {
    google()
    mavenLocal()
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://jetbrains.bintray.com/trove4j")
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.ui)
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.desktop.currentOs)
    implementation("com.squareup:kotlinpoet:1.10.2")
    implementation("br.com.devsrsouza:svg-to-compose:0.7.0")
    implementation("libetal.libraries.compose.layouts:text:1.0.0")
    implementation("libetal.libraries.compose.layouts:icons:1.0.0")
    implementation("libetal.kotlin.compose:narrator-desktop:1.0.1-SNAPSHOT")
    implementation("br.com.devsrsouza.compose.icons.jetbrains:font-awesome:1.0.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    }
}

compose.desktop {
    application {
        mainClass = "libetal.applications.svg2compose.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "iconConverter"
            packageVersion = "1.0.0"
        }
    }
}