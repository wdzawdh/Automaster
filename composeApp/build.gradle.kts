import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    wasmJs {
        moduleName = "composeApp"
        browser {
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            kotlin.srcDir("build/generated/buildConfig")
        }

        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

android {
    namespace = "com.cw.automaster"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.cw.automaster"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

compose.desktop {
    application {
        mainClass = "com.cw.automaster.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg)
            packageName = "Automaster"
            packageVersion = "1.0.0"

            macOS {
                bundleID = "com.cw.automaster"
                iconFile.set(project.file("launcher/logo.icns"))
                dockName = packageName
                /*
                entitlementsFile.set(project.layout.projectDirectory.file("src/desktopMain/resources/sandbox.entitlements"))
                signing {
                    sign.set(true)
                    identity = "Developer ID Application: Your Name (Team ID)"
                }
                */
            }
        }
    }
}

// ------------------------------创建BuildConfig获取配置----------------------------------------------
tasks.register("generateBuildConfig") {
    val outputDir = layout.buildDirectory.dir("generated/buildConfig")
    val versionName = project.findProperty("VERSION_NAME") ?: "Unknown"
    inputs.property("versionName", versionName)
    outputs.dir(outputDir)

    doLast {
        val buildConfigDir = outputDir.get().asFile
        buildConfigDir.mkdirs()

        val buildConfigFile = buildConfigDir.resolve("BuildConfig.kt")
        buildConfigFile.writeText(
            """
            package com.cw.automaster

            object BuildConfig {
                const val VERSION_NAME = "$versionName"
            }
            """.trimIndent()
        )
    }
}
tasks.matching { it.name.startsWith("compileKotlin") }.configureEach {
    dependsOn("generateBuildConfig")
}

// ------------------------------打包app时将dylib库打入包中--------------------------------------------
tasks.register<Copy>("copyPackageLibs") {
    from("$buildDir/libs")
    include("*.dylib")
    into("$buildDir/compose/tmp/main/runtime/lib")
    dependsOn("compileDynamicLibs")
}
tasks.matching { it.name == "prepareAppResources" }.configureEach {
    dependsOn("copyPackageLibs")
}

// ----------------------desktopRun时将dylib库加到java.library.path-----------------------------------
tasks.register<Copy>("copyRunLibs") {
    from("$buildDir/libs")
    include("*.dylib")
    into("${System.getProperty("user.home")}/Library/Java/Extensions")
    dependsOn("compileDynamicLibs")
}
tasks.matching { it.name == "desktopRun" }.configureEach {
    dependsOn("copyRunLibs")
}

// -------------------------------编译前打包dylib动态库------------------------------------------------
val sourceDir = file("src/desktopMain/jni")
val compileTasks = mutableListOf<TaskProvider<Exec>>()
val sourceFiles = sourceDir.listFiles { file -> file.extension == "m" }?.toList() ?: emptyList()
sourceFiles.forEach { sourceFile ->
    val libName = "lib${sourceFile.nameWithoutExtension}.dylib"
    val outputDir = file("$buildDir/libs")
    val outputLib = file("${outputDir}/${libName}")
    val compileTask = tasks.register<Exec>("compile${sourceFile.nameWithoutExtension}DynamicLib") {
        commandLine = listOf(
            "clang", "-dynamiclib", "-o", outputLib.absolutePath, sourceFile.absolutePath,
            "-I${System.getenv("JAVA_HOME")}/include",
            "-I${System.getenv("JAVA_HOME")}/include/darwin",
            "-framework", "Cocoa",
            "-framework", "Carbon",
            "-framework", "ServiceManagement",
        )
        // 检查是否需要重新编译
        outputs.file(outputLib)
        inputs.file(sourceFile)
        outputs.upToDateWhen {
            outputLib.exists() && sourceFile.lastModified() <= outputLib.lastModified()
        }
        // 创建输出目录
        doFirst {
            outputDir.mkdirs()
        }
    }
    // 添加到编译任务列表
    compileTasks.add(compileTask)
}
// 注册compileDynamicLibs任务
tasks.register("compileDynamicLibs") {
    dependsOn(compileTasks)
}
// 让其他任务依赖compileDynamicLibs任务
tasks.matching { it.name.startsWith("compileKotlin") }.configureEach {
    dependsOn("compileDynamicLibs")
}
