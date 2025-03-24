plugins {
    id("org.jetbrains.kotlin.jvm")
    id("kotlin-kapt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation("com.squareup:kotlinpoet:1.14.2")
    implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.7.0")

    kapt("com.google.auto.service:auto-service:1.1.1")
    implementation("com.google.auto.service:auto-service-annotations:1.1.1")

    implementation(project(":annotations"))
}