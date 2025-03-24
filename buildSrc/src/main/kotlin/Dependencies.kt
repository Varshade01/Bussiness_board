object Dependencies {

    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    const val junit = "junit:junit:${Versions.junit}"

    object AndroidX {
        const val core = "androidx.core:core-ktx:${Versions.AndroidX.core_ktx}"
        const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.AndroidX.lifecycle_runtime_ktx}"
        const val test_junit_ext = "androidx.test.ext:junit:${Versions.AndroidX.androidx_test_ext_junit}"
        const val test_espresso = "androidx.test.espresso:espresso-core:${Versions.AndroidX.espresso_core}"
        const val material3 = "androidx.compose.material3:material3:${Versions.AndroidX.material3}"
        const val material = "androidx.compose.material:material:${Versions.AndroidX.material}"
        const val splashscreen = "androidx.core:core-splashscreen:${Versions.AndroidX.splashscreen}"
    }

    object Compose {
        const val activity = "androidx.activity:activity-compose:${Versions.Compose.activity}"
        const val ui = "androidx.compose.ui:ui:${Versions.Compose.version}"
        const val ui_tooling = "androidx.compose.ui:ui-tooling:${Versions.Compose.version}"
        const val ui_tooling_preview = "androidx.compose.ui:ui-tooling-preview:${Versions.Compose.version}"
        const val ui_test = "androidx.compose.ui:ui-test-manifest:${Versions.Compose.version}"
        const val ui_graphics = "androidx.compose.ui:ui-graphics:${Versions.Compose.version}"
        const val bom = "androidx.compose:compose-bom:${Versions.Compose.bom}"
        const val navigation = "androidx.navigation:navigation-compose:${Versions.Compose.navigation}"
        const val hilt_navigation = "androidx.hilt:hilt-navigation-compose:${Versions.Compose.hilt_navigation}"
    }

    object Firebase {
        const val core = "com.google.firebase:firebase-core:${Versions.Firebase.core}"
        const val analytics = "com.google.firebase:firebase-analytics:${Versions.Firebase.analytics}"
        const val bom = "com.google.firebase:firebase-bom:${Versions.Firebase.bom}"
        const val database = "com.google.firebase:firebase-database-ktx:${Versions.Firebase.database}"
        const val storage = "com.google.firebase:firebase-storage-ktx:${Versions.Firebase.storage}"
        const val auth = "com.google.firebase:firebase-auth-ktx:${Versions.Firebase.auth}"
        const val messaging="com.google.firebase:firebase-messaging:${Versions.Firebase.messaging}"
        const val messaging_ktx="com.google.firebase:firebase-messaging-ktx:${Versions.Firebase.messaging}"
        const val analytics_ktx = "com.google.firebase:firebase-analytics:${Versions.Firebase.analytics_ktx}"
        const val dynamic_links = "com.google.firebase:firebase-dynamic-links:${Versions.Firebase.dynamic_links}"
    }

    object Room {
        const val room_runtime = "androidx.room:room-runtime:${Versions.AndroidX.room}"
        const val room_ktx = "androidx.room:room-ktx:${Versions.AndroidX.room}"
        const val room_compiler = "androidx.room:room-compiler:${Versions.AndroidX.room}"
    }

    object DataStore{
        const val data_store = "androidx.datastore:datastore-preferences:${Versions.DataStore.datastore_versions}"
    }

    object Hilt {
        const val android = "com.google.dagger:hilt-android:${Versions.Hilt.version}"
        const val compiler = "com.google.dagger:hilt-compiler:${Versions.Hilt.version}"
        const val compiler_kapt = "androidx.hilt:hilt-compiler:${Versions.Hilt.work_version}"
        const val work = "androidx.hilt:hilt-work:${Versions.Hilt.work_version}"
    }

    object Lifecycle {
        const val core = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.Lifecycle.core}"
        const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.Lifecycle.core}"
        const val viewmodel_savedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.Lifecycle.core}"
    }

    object Google {
        const val accompanist_systemuicontroller =
            "com.google.accompanist:accompanist-systemuicontroller:${Versions.Google.accompanist}"
    }

    object Coil {
        const val compose = "io.coil-kt:coil-compose:${Versions.Coil.compose}"
    }

    object WorkManager {
        const val work_runtime = "androidx.work:work-runtime-ktx:${Versions.WorkManager.work_version}"
        const val work_gcm = "androidx.work:work-gcm:${Versions.WorkManager.work_version}"
        const val work_testing = "androidx.work:work-testing:${Versions.WorkManager.work_version}"
        const val work_multiprocess = "androidx.work:work-multiprocess:${Versions.WorkManager.work_version}"
    }
}