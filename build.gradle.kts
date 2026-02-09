plugins {
    kotlin("multiplatform") version "1.9.20"
    kotlin("native.cocoapods") version "1.9.20"
    id("com.android.application") version "8.1.4"
    id("org.jetbrains.compose") version "1.5.12"
}

kotlin {
    androidTarget()

    jvm("desktop")

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        version = "1.0.0"
        summary = "A notebook journal application"
        homepage = "https://github.com/example/journal"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "JournalShared"
            isStatic = true
        }
    }

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.uiTooling)
            api("cafe.adriel.voyager:voyager-navigator:1.0.0")
            api("cafe.adriel.voyager:voyager-bottom-sheet-navigator:1.0.0")
            api("cafe.adriel.voyager:voyager-tab-navigator:1.0.0")
            api("cafe.adriel.voyager:voyager-transitions:1.0.0")
            implementation("io.insert-koin:koin-core:3.5.3")
            // Temporarily commented out due to resolution issues
            // implementation("io.insert-koin:koin-compose:3.5.6")
            // implementation("io.insert-koin:koin-compose-viewmodel:3.5.6")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            implementation("androidx.lifecycle:lifecycle-viewmodel:2.7.0")
        }
        
        androidMain.dependencies {
            implementation("androidx.activity:activity-compose:1.8.1")
            implementation("androidx.appcompat:appcompat:1.6.1")
            implementation("androidx.core:core-ktx:1.12.0")
        }
        
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

android {
    namespace = "com.example.journal"
    compileSdk = 34

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/androidMain/resources")

    defaultConfig {
        applicationId = "com.example.journal"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}