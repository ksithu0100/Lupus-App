plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.example.lupusapp"

    // 🔥 REQUIRED FIX — Update compileSdk & targetSdk to 36
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.lupusapp"
        minSdk = 24
        targetSdk = 36       // 🔥 Must match compileSdk
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)

    // 🔥 FIX — Use ONLY TOML versions (2.7.7), remove mixed versions
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Activity support
    implementation(libs.activity)

    // Calendar library (AndroidX compatible)
    implementation("com.github.kizitonwose:CalendarView:2.0.3")


    // Firebase
    implementation(platform(libs.google.firebase.bom))
    implementation(libs.firebase.auth)

    implementation(libs.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
