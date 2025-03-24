// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        google()  // Google"s Maven repository
        mavenCentral()  // Maven Central repository

    }
    dependencies {
        classpath("com.google.gms:google-services:4.3.15")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:${Versions.Hilt.version}")
    }

}

plugins {
    id(Plugins.android) version Versions.agp apply false
    id(Plugins.kotlin) version Versions.kotlin apply false
    id(Plugins.hilt) version Versions.Hilt.version apply false
}
