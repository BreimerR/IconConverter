import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val projectVersion: String by project
val projectGroup: String by project

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = projectGroup
version = projectVersion

allprojects {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://jetbrains.bintray.com/trove4j")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

}

dependencies {
    implementation(compose.ui)
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.desktop.currentOs)

    implementation("libetal.libraries.compose.layouts:text-desktop:1.0.0")

    implementation("libetal.libraries.compose.layouts:icons-desktop:1.0.0")
    implementation("libetal.libraries.compose.layouts:dropdown-desktop:1.0.0")

    implementation("libetal.libraries.compose.narrator:core-jvm-desktop:1.1.1610-ALPHA")
    implementation("libetal.libraries.compose.narrator:lifecycle-aware-desktop:1.1.1610-ALPHA")

    implementation("com.squareup:kotlinpoet:1.12.0")
    implementation("br.com.devsrsouza:svg-to-compose:0.8.2")
    implementation("br.com.devsrsouza.compose.icons.jetbrains:font-awesome:1.0.0")

    testImplementation(kotlin("test"))

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
    }
}

compose.desktop {
    application {

        mainClass = "libetal.applications.assetor.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Rpm)

            packageName = "Assetor"
            packageVersion = projectVersion

            linux {
                debMaintainer = "brymher@gmail.com"
                appCategory = "Vector Graphics;Design;Development"


                // a version for all Linux distributables
                packageVersion = projectVersion
                // a version only for the deb package
                debPackageVersion = projectVersion
                // a version only for the rpm package
                rpmPackageVersion = projectVersion
            }

        }

        description = """|
            |Icon converter using Svg2Compose library
            |Browse icons on your pc
        """.trimMargin()

    }
}

tasks.create("updateAssets") {
    group = "build"
    dependsOn(
        "build",
        "packageDeb",
        "packageDmg",
        "packageMsi"
    )

    doLast {

        val file = File(buildDir, "compose/binaries/main")
        val assetsDir = File(project.rootDir, "assets")

        assetsDir.listFiles()?.forEach { plausiblePackageFile ->
            if (!plausiblePackageFile.isDirectory) {
                when (plausiblePackageFile.extension) {
                    "dep", "msi", "dmg", "exe" -> plausiblePackageFile.delete()
                }
            }
        }

        file.listFiles()?.forEach { directory ->

            if (directory.isDirectory) {
                directory.listFiles()?.forEach { packagedFile ->
                    packagedFile.inputStream().use { inputStream ->
                        File(assetsDir, "assetor.${packagedFile.extension}").outputStream().use { outputStream ->
                            outputStream.write(inputStream.readAllBytes())
                        }
                    }
                }
            }

        }

    }

}