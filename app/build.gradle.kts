plugins {
    id(Plugins.android)
    id(Plugins.kotlin)
    id(Plugins.google_services)
    id(Plugins.kapt)
    id(Plugins.hilt)
    id(Plugins.parcelize)
}

android {
    namespace = "com.rdua.whiteboard"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rdua.whiteboard"
        minSdk = 28
        targetSdk = 34
        versionCode = 2
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Google
    implementation(Dependencies.Google.accompanist_systemuicontroller)

    // Lifecycle
    implementation(Dependencies.Lifecycle.core)
    implementation(Dependencies.Lifecycle.viewmodel)
    implementation(Dependencies.Lifecycle.viewmodel_savedState)

    // Hilt
    implementation(Dependencies.Hilt.android)
    implementation(Dependencies.Hilt.work)
    kapt(Dependencies.Hilt.compiler)
    kapt(Dependencies.Hilt.compiler_kapt)

    // Compose
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.ui_graphics)
    implementation(Dependencies.Compose.activity)
    implementation(Dependencies.Compose.navigation)
    implementation(Dependencies.Compose.hilt_navigation)
    implementation(Dependencies.Compose.ui_tooling_preview)
    debugImplementation(Dependencies.Compose.ui_tooling)
    debugImplementation(Dependencies.Compose.ui_test)
    androidTestImplementation(Dependencies.Compose.bom)

    // Firebase
    implementation(Dependencies.Firebase.core)
    implementation(Dependencies.Firebase.analytics)
    implementation(Dependencies.Firebase.bom)
    implementation(Dependencies.Firebase.database)
    implementation(Dependencies.Firebase.storage)
    implementation(Dependencies.Firebase.auth)
    implementation(Dependencies.Firebase.messaging)
    implementation(Dependencies.Firebase.messaging_ktx)
    implementation(Dependencies.Firebase.dynamic_links)

    // Room
    implementation(Dependencies.Room.room_runtime)
    implementation(Dependencies.Room.room_ktx)
    kapt(Dependencies.Room.room_compiler)

    // Core
    implementation(Dependencies.kotlin)
    implementation(Dependencies.AndroidX.core)
    implementation(Dependencies.AndroidX.lifecycle)
    implementation(Dependencies.AndroidX.material)
    implementation(Dependencies.AndroidX.material3)
    implementation(Dependencies.AndroidX.splashscreen)
    testImplementation(Dependencies.junit)
    androidTestImplementation(Dependencies.AndroidX.test_junit_ext)
    androidTestImplementation(Dependencies.AndroidX.test_espresso)

    // Coil
    implementation(Dependencies.Coil.compose)

    // Data Store
    implementation(Dependencies.DataStore.data_store)

    // WorkManager
    implementation(Dependencies.WorkManager.work_runtime)
    implementation(Dependencies.WorkManager.work_gcm)
    implementation(Dependencies.WorkManager.work_testing)
    implementation(Dependencies.WorkManager.work_multiprocess)

    // Modules
    kapt(project(":code-gen"))
    implementation(project(":annotations"))
}