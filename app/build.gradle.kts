plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.swordlibrary"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.swordlibrary"
        minSdk = 19
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(project(":logger"))
    
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.core.ktx)

    //日志显示库：https://github.com/getActivity/Logcat
    debugImplementation(libs.logcat)
}