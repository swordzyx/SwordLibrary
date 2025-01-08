import com.android.build.gradle.tasks.BundleAar

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

var versionName = "1.0.0"
android {
    namespace = "com.sword.logger"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

tasks.withType<BundleAar>().configureEach {
    val fileName = "${project.name}-${versionName}.aar"
    archiveFileName.set(fileName)
}


dependencies {
    implementation(libs.kotlinx.coroutines.core)
}