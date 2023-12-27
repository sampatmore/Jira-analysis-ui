import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs {
//       browser()
//    }
    
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    
    jvm()
    
    sourceSets {
        commonMain.dependencies {
            implementation("io.ktor:ktor-client-cio:2.3.7")
            implementation("io.ktor:ktor-client-core:2.3.7")
            implementation("io.ktor:ktor-client-json:2.3.7")
            implementation("io.ktor:ktor-client-logging:2.3.7")
            implementation("io.ktor:ktor-client-serialization:2.3.7")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
            implementation("io.ktor:ktor-client-auth:2.3.7")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")

            implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.0")
        }
    }
}

android {
    namespace = "org.soenergy.jira.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
