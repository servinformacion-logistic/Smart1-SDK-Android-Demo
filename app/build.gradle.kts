import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.realm)
}

android {
    namespace = "com.servinformacion.smart1sdkdemo"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.servinformacion.smart1sdkdemo"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        all {
            val googleMapsApiKey = gradleLocalProperties(rootDir, providers)
                .getProperty("GOOGLE_MAPS_API_KEY")
            resValue("string","GOOGLE_MAPS_API_KEY", "\"$googleMapsApiKey\"")
            
            val smart1ApiKey = gradleLocalProperties(rootDir, providers)
                .getProperty("SMART1_SDK_API_KEY")
            buildConfigField("String", "smart1SDKApiKey", "\"$smart1ApiKey\"")
            
            val smart1Email = gradleLocalProperties(rootDir, providers)
                .getProperty("SMART1_SDK_USER_OPERATOR_EMAIL")
            buildConfigField("String", "smart1SDKUserOperatorEmail", "\"$smart1Email\"")
        }
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
    buildFeatures {
        compose = true
        buildConfig = true
    }
    
    packaging {
        resources {
            excludes += "/META-INF/ASL2.0"
            excludes += "/META-INF/LICENSE"
            excludes += "/META-INF/NOTICE"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.timber)
    implementation(libs.google.maps.compose)
    implementation(libs.play.services.location)
    implementation(libs.google.accompanist.permissions)
    implementation(libs.smart1.sdk)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.auth)
    implementation(libs.russhwolf.settings)
    implementation(libs.russhwolf.settings.coroutines)
    implementation(libs.realm.kotlin)
    implementation(libs.apache.avro)
    implementation(libs.androidx.security)
}