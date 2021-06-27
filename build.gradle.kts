plugins {
    kotlin("jvm") version "1.4.32"
    application
    id("org.openjfx.javafxplugin") version "0.0.9"
    id("org.beryx.runtime") version "1.12.5"
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("io.andrewohara.betelgeuse.Betelgeuse")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation("redis.clients:jedis:3.6.1")
    implementation("com.squareup.moshi:moshi-kotlin:1.12.0")
    implementation ("org.slf4j:slf4j-simple:1.7.31")
    implementation("com.github.fppt:jedis-mock:0.1.20")
    implementation("com.jcraft:jsch:0.1.55")
}

javafx {
    version = "16"
    modules = listOf("javafx.controls")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

runtime {
    addOptions("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages")

// Uncomment and adjust the code below if you want to generate images for multiple platforms.
// (You need to also uncomment the line 'targetPlatformName = ...' in the jpackage block.)
/*
    targetPlatform("lin") {
        jdkHome = jdkDownload("https://github.com/AdoptOpenJDK/openjdk15-binaries/releases/download/jdk-15.0.2%2B7/OpenJDK15U-jdk_x64_linux_hotspot_15.0.2_7.tar.gz")
    }
    targetPlatform("mac") {
        jdkHome = jdkDownload("https://github.com/AdoptOpenJDK/openjdk15-binaries/releases/download/jdk-15.0.2%2B7/OpenJDK15U-jdk_x64_mac_hotspot_15.0.2_7.tar.gz") {
            downloadDir = "$buildDir/myMac"
            archiveName = "my-mac-jdk"
            archiveExtension = "tar.gz"
            pathToHome = "jdk-15.0.2+7/Contents/Home"
            overwrite = true
        }
    }
    targetPlatform("win") {
        jdkHome = jdkDownload("https://github.com/AdoptOpenJDK/openjdk15-binaries/releases/download/jdk-15.0.2%2B7/OpenJDK15U-jdk_x64_windows_hotspot_15.0.2_7.zip")
    }
*/

    launcher {
        noConsole = true
    }
    jpackage {
        // Uncomment and adjust the following line if your runtime task is configured to generate images for multiple platforms
        // targetPlatformName = "mac"

        val currentOs = org.gradle.internal.os.OperatingSystem.current()
//        val imgType = when {
//            currentOs.isWindows -> "ico"
//            currentOs.isMacOsX -> "icns"
//            else -> "png"
//        }
//
//        imageOptions.addAll(listOf("--icon", "src/main/resources/betelgeuse.$imgType"))

        installerOptions.addAll(listOf(
            "--resource-dir", "src/main/resources",
            "--vendor", "andrewohara.io"
        ))

        val osOptions = when {
            currentOs.isWindows -> listOf("--win-per-user-install", "--win-dir-chooser", "--win-menu", "--win-shortcut")
            currentOs.isLinux -> listOf("--linux-package-name", "betelgeuse", "--linux-shortcut")
            currentOs.isMacOsX -> listOf("--mac-package-name", "betelgeuse")
            else -> listOf()
        }
        installerOptions.addAll(osOptions)
    }
}