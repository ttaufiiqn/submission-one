    plugins {
        alias(libs.plugins.android.application)
        alias(libs.plugins.kotlin.android)
        id("com.google.devtools.ksp")
        id("kotlin-parcelize")
    }

    android {
        namespace = "com.dicoding.subtest"
        compileSdk = 34

        defaultConfig {
            applicationId = "com.dicoding.subtest"
            minSdk = 24
            targetSdk = 34
            versionCode = 1
            versionName = "1.0"

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            buildConfigField("String", "BASE_URL", "\"https://event-api.dicoding.dev/\"")
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
            viewBinding = true
            buildConfig = true
        }
    }

    dependencies {

        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.material)
        implementation(libs.androidx.activity)
        implementation(libs.androidx.constraintlayout)
        implementation(libs.androidx.datastore.core.android)
        implementation(libs.androidx.ui.desktop)
        implementation(libs.androidx.room.common)
        implementation(libs.androidx.room.ktx)
        implementation(libs.androidx.recyclerview)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        implementation(libs.glide)
        implementation(libs.retrofit2.retrofit)
        implementation(libs.converter.gson)
        implementation(libs.logging.interceptor)
        implementation(libs.androidx.navigation.fragment.ktx)
        implementation(libs.androidx.navigation.ui.ktx)
        implementation(libs.androidx.lifecycle.viewmodel.ktx)
        implementation(libs.androidx.lifecycle.livedata.ktx)
        implementation(libs.androidx.fragment.ktx)
        implementation(libs.androidx.lifecycle.viewmodel.ktx)
        implementation(libs.androidx.lifecycle.livedata.ktx)
        implementation(libs.activity.ktx)
        implementation(libs.androidx.fragment.ktx)
        implementation(libs.kotlinx.coroutines.android)
        implementation(libs.androidx.datastore.preferences)
        implementation(libs.androidx.lifecycle.viewmodel.ktx)
        implementation(libs.androidx.lifecycle.livedata.ktx)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.coroutines.android.v173)
        implementation (libs.androidx.datastore.preferences.v111)
        implementation(libs.androidx.lifecycle.viewmodel.ktx)
        implementation(libs.androidx.lifecycle.livedata.ktx)
        ksp(libs.room.compiler)

        implementation (libs.androidx.navigation.fragment.ktx.v283) // Use the latest version
        implementation (libs.androidx.navigation.ui.ktx.v283) // Use the latest version
        implementation (libs.kotlinx.coroutines.core)
        implementation (libs.kotlinx.coroutines.android.v173)

    }